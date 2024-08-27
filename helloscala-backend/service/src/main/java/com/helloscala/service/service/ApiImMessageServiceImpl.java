package com.helloscala.service.service;


import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.service.entity.ImMessage;
import com.helloscala.service.entity.ImRoom;
import com.helloscala.service.entity.User;
import com.helloscala.service.enums.YesOrNoEnum;
import com.helloscala.service.mapper.ImMessageMapper;
import com.helloscala.service.mapper.ImRoomMapper;
import com.helloscala.service.mapper.UserMapper;
import com.helloscala.common.utils.BeanCopyUtil;
import com.helloscala.common.utils.DateUtil;
import com.helloscala.common.utils.IpUtil;
import com.helloscala.common.utils.PageUtil;
import com.helloscala.common.vo.message.ImMessageVO;
import com.helloscala.common.vo.message.ImRoomListVO;
import com.helloscala.common.vo.user.ImOnlineUserVO;
import com.helloscala.common.vo.user.UserInfoVO;
import com.helloscala.common.web.exception.BadRequestException;
import com.helloscala.common.web.exception.ConflictException;
import com.helloscala.web.handle.RelativeDateFormat;
import com.helloscala.web.im.MessageConstant;
import com.helloscala.web.service.ApiImMessageService;
import com.helloscala.web.websocket.ChatWebSocket;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class ApiImMessageServiceImpl implements ApiImMessageService {
    private static final Pattern pattern = Pattern.compile("(http|https)://[\\w\\d.-]+(/[\\w\\d./?=#&-]*)?");
    private final ImMessageMapper imMessageMapper;
    private final ImRoomMapper imRoomMapper;
    private final UserMapper userMapper;
    private final ChatWebSocket chatWebSocket;

    @Override
    public Page<ImMessageVO> selectHistoryList() {
        Page<ImMessageVO> page = new Page<>(PageUtil.getPageNo(), PageUtil.getPageSize());
        Page<ImMessageVO> pageResult = imMessageMapper.selectPublicHistoryList(page);
        formatCreateTime(pageResult);
        return pageResult;
    }

    @Override
    public List<ImOnlineUserVO> selectOnlineUserList(Set<String> keys) {
        return imMessageMapper.selectPublicOnlineUserList(keys);
    }

    @Override
    public Page<ImMessageVO> selectUserImHistoryList(String fromUserId, String toUserId) {
        Page<ImMessageVO> page = new Page<>(PageUtil.getPageNo(), PageUtil.getPageSize());
        Page<ImMessageVO> pageResult = imMessageMapper.selectPublicUserImHistoryList(page,
                fromUserId, toUserId);
        formatCreateTime(pageResult);
        return pageResult;
    }

    @Override
    public List<ImRoomListVO> selectRoomList() {
        List<ImRoomListVO> list = new ArrayList<>();
        LambdaQueryWrapper<ImRoom> imRoomQuery = new LambdaQueryWrapper<ImRoom>().eq(ImRoom::getFromUserId, StpUtil.getLoginIdAsString())
                .orderByDesc(ImRoom::getCreateTime);
        List<ImRoom> imRooms = imRoomMapper.selectList(imRoomQuery);

        Set<String> userIdSet = imRooms.stream().map(ImRoom::getToUserId).collect(Collectors.toSet());
        List<User> users = ObjectUtil.isEmpty(userIdSet) ? new ArrayList<>() : userMapper.selectBatchIds(userIdSet);
        Map<String, User> userMap = users.stream().collect(Collectors.toMap(User::getId, Function.identity()));

        for (ImRoom imRoom : imRooms) {
            User user = userMap.get(imRoom.getToUserId());
            int readNum = imMessageMapper.selectListReadByUserId(user.getId(), StpUtil.getLoginIdAsString());

            ImRoomListVO vo = ImRoomListVO.builder().id(imRoom.getId()).receiveId(user.getId()).nickname(user.getNickname())
                    .avatar(user.getAvatar()).createTimeStr(RelativeDateFormat.format(imRoom.getCreateTime())).build();
            vo.setReadNum(readNum);
            list.add(vo);
        }
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ImRoomListVO addRoom(String toUserId) {
        String fromUserId = StpUtil.getLoginIdAsString();
        if (StringUtils.isBlank(toUserId)) {
            throw new BadRequestException("Please choose an user!");
        }
        if (toUserId.equals(fromUserId)) {
            throw new BadRequestException("Can not chat with your self!");
        }
        ImRoom imRoom = imRoomMapper.selectOne(new LambdaQueryWrapper<ImRoom>().eq(ImRoom::getFromUserId, fromUserId)
                .eq(ImRoom::getToUserId, toUserId));
        if (imRoom != null) {
            throw new ConflictException("Room exist!");
        }
        imRoom = ImRoom.builder().type(1).fromUserId(fromUserId).toUserId(toUserId).build();
        imRoomMapper.insert(imRoom);
        UserInfoVO userInfoVO = userMapper.selectInfoByUserId(toUserId);
        ImRoomListVO vo = ImRoomListVO.builder().id(imRoom.getId()).receiveId(toUserId).nickname(userInfoVO.getNickname())
                .avatar(userInfoVO.getAvatar()).createTimeStr(RelativeDateFormat.format(imRoom.getCreateTime())).build();
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void read(String userId) {
        imMessageMapper.updateRead(userId, StpUtil.getLoginIdAsString());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRoom(String roomId) {
        imRoomMapper.deleteById(roomId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void chat(ImMessageVO obj) {
        Matcher matcher = pattern.matcher(obj.getContent());
        String content = obj.getContent();
        String url = null;
        try {
            while (matcher.find()) {
                url = matcher.group();
                if (url.contains("res.wx.qq.com")
                        || url.contains("npm.elemecdn.com")
                        || url.contains("helloscala")
                ) {
                    continue;
                }
                Document doc = Jsoup.connect(url).get();
                String hrefContent = String.format("<a href=\"%s\" target=\"_blank\" >%s</a>", url, doc.title());
                content = content.replace(url, hrefContent);
            }
        } catch (IOException e) {
            log.error("Failed to deserialize message, url={}", url, e);
        }
//        if (obj.getType() != MessageConstant.MESSAGE_TYPE_IMG) {
//            String filterContent = SensitiveUtils.filter(content);
//            obj.setContent(filterContent);
//        }

        obj.setIp(IpUtil.getIp());
        obj.setIpSource(IpUtil.getIp2region(obj.getIp()));
        obj.setContent(content);
        ImMessage imMessage = BeanCopyUtil.copyObject(obj, ImMessage.class);
        //保存消息到数据库
        imMessageMapper.insert(imMessage);
        //如果是私聊，则发送聊天的同时给发送人创建房间
        if (obj.getCode() == MessageConstant.PRIVATE_CHAT_CODE) {
            ImRoom imRoom = imRoomMapper.selectOne(new LambdaQueryWrapper<ImRoom>().eq(ImRoom::getFromUserId, obj.getToUserId())
                    .eq(ImRoom::getToUserId, obj.getFromUserId()));
            if (imRoom == null) {
                ImRoom room = ImRoom.builder().fromUserId(obj.getToUserId()).toUserId(obj.getFromUserId()).type(obj.getCode()).build();
                imRoomMapper.insert(room);
            }
        }

        obj.setId(imMessage.getId());
        obj.setCreateTimeStr(RelativeDateFormat.format(imMessage.getCreateTime()));
        chatWebSocket.chat(obj);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void withdraw(ImMessageVO message) {
        ImMessage entity = imMessageMapper.selectById(message.getId());
        if (DateUtil.getDiffDateToMinutes(entity.getCreateTime(), DateUtil.getNowDate()) >= 2) {
            throw new BadRequestException("recall message failed, message sent over 2 minutes!");
        }
        if (!entity.getFromUserId().equals(StpUtil.getLoginIdAsString())) {
            throw new BadRequestException("Can only recall your own message!");
        }
        ImMessage imMessage = BeanCopyUtil.copyObject(message, ImMessage.class);
        imMessage.setIp(IpUtil.getIp());
        imMessage.setIpSource(IpUtil.getIp2region(imMessage.getIp()));
        imMessageMapper.updateById(imMessage);
        chatWebSocket.chat(message);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Page<ImMessageVO> getMessageNotice(Integer type) {
        Page<Object> page = new Page<>(PageUtil.getPageNo(), PageUtil.getPageSize());
        Page<ImMessageVO> pageResult = imMessageMapper.getMessageNotice(page, StpUtil.getLoginIdAsString(), type);

        pageResult.getRecords().forEach(item -> item.setCreateTimeStr(RelativeDateFormat.format(item.getCreateTime())));
        ImMessage message = ImMessage.builder().isRead(1).build();
        LambdaQueryWrapper<ImMessage> updateWrapper = new LambdaQueryWrapper<>();
        updateWrapper.eq(ImMessage::getToUserId, StpUtil.getLoginIdAsString())
                .eq(ImMessage::getNoticeType, type);
        imMessageMapper.update(message, updateWrapper);
        return pageResult;
    }

    @Override
    public Map<String, Long> getNewSystemNotice() {
        Long systemCount = imMessageMapper.selectCount(new LambdaQueryWrapper<ImMessage>()
                .eq(ImMessage::getCode, MessageConstant.SYSTEM_MESSAGE_CODE).eq(ImMessage::getToUserId, StpUtil.getLoginIdAsString())
                .eq(ImMessage::getIsRead, 0).eq(ImMessage::getNoticeType, MessageConstant.MESSAGE_SYSTEM_NOTICE));

        Long commentCount = imMessageMapper.selectCount(new LambdaQueryWrapper<ImMessage>()
                .eq(ImMessage::getCode, MessageConstant.SYSTEM_MESSAGE_CODE).eq(ImMessage::getToUserId, StpUtil.getLoginIdAsString())
                .eq(ImMessage::getIsRead, 0).eq(ImMessage::getNoticeType, MessageConstant.MESSAGE_COMMENT_NOTICE));

        Long privateCount = imMessageMapper.selectCount(new LambdaQueryWrapper<ImMessage>()
                .eq(ImMessage::getCode, MessageConstant.PRIVATE_CHAT_CODE).eq(ImMessage::getToUserId, StpUtil.getLoginIdAsString())
                .eq(ImMessage::getIsRead, 0));

        Map<String, Long> map = new HashMap<>();
        map.put("system", systemCount);
        map.put("comment", commentCount);
        map.put("private", privateCount);
        return map;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByNoticeType(String id, Integer type) {
        if (StringUtils.isNotBlank(id)) {
            imMessageMapper.deleteById(id);
            return;
        }
        imMessageMapper.delete(new LambdaQueryWrapper<ImMessage>().eq(ImMessage::getToUserId, StpUtil.getLoginIdAsString())
                .eq(ImMessage::getNoticeType, type));
    }

    @Override
    public Page<ImMessageVO> getMessageNoticeApplet(Integer type) {
        Page<Object> page = new Page<>(PageUtil.getPageNo(), PageUtil.getPageSize());
        Page<ImMessageVO> pageResult = imMessageMapper.getMessageNotice(page, StpUtil.getLoginIdAsString(), type);
        pageResult.getRecords().forEach(item -> item.setCreateTimeStr(RelativeDateFormat.format(item.getCreateTime())));
        return pageResult;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void markReadMessageNoticeApplet(String id) {
        if (StringUtils.isNotBlank(id)) {
            imMessageMapper.updateById(ImMessage.builder().id(id).isRead(YesOrNoEnum.YES.getCode()).build());
            return;
        }
        LambdaQueryWrapper<ImMessage> updateWrapper = new LambdaQueryWrapper<>();
        updateWrapper.eq(ImMessage::getToUserId, StpUtil.getLoginIdAsString());
        imMessageMapper.update(ImMessage.builder().isRead(YesOrNoEnum.YES.getCode()).build(), updateWrapper);
    }

    private void formatCreateTime(Page<ImMessageVO> page) {
        page.getRecords().forEach(item -> item.setCreateTimeStr(RelativeDateFormat.format(item.getCreateTime())));
    }
}
