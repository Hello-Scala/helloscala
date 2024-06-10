package com.helloscala.web.service.impl;


import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.common.ResponseResult;
import com.helloscala.common.entity.ImMessage;
import com.helloscala.common.entity.ImRoom;
import com.helloscala.common.enums.YesOrNoEnum;
import com.helloscala.common.exception.BusinessException;
import com.helloscala.common.mapper.ImMessageMapper;
import com.helloscala.common.mapper.ImRoomMapper;
import com.helloscala.common.mapper.UserMapper;
import com.helloscala.common.utils.BeanCopyUtil;
import com.helloscala.common.utils.DateUtil;
import com.helloscala.common.utils.IpUtil;
import com.helloscala.common.utils.PageUtil;
import com.helloscala.common.vo.message.ImMessageVO;
import com.helloscala.common.vo.message.ImRoomListVO;
import com.helloscala.common.vo.user.ImOnlineUserVO;
import com.helloscala.common.vo.user.UserInfoVO;
import com.helloscala.web.handle.RelativeDateFormat;
import com.helloscala.web.im.MessageConstant;
import com.helloscala.web.im.WebSocketInfoService;
import com.helloscala.web.service.ApiImMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



@Slf4j
@Service
@RequiredArgsConstructor
public class ApiImMessageServiceImpl implements ApiImMessageService {
    private static final Pattern pattern = Pattern.compile("(http|https)://[\\w\\d.-]+(/[\\w\\d./?=#&-]*)?");
    private final ImMessageMapper imMessageMapper;
    private final ImRoomMapper imRoomMapper;
    private final UserMapper userMapper;
    private final WebSocketInfoService webSocketInfoService;

    @Override
    public ResponseResult selectHistoryList() {
        Page<ImMessageVO> page = imMessageMapper.selectPublicHistoryList(new Page<>(PageUtil.getPageNo(),
                PageUtil.getPageSize()));
        formatCreateTime(page);
        return ResponseResult.success(page);
    }

    @Override
    public List<ImOnlineUserVO> selectOnlineUserList(Set<String> keys) {
        return imMessageMapper.selectPublicOnlineUserList(keys);
    }

    @Override
    public ResponseResult selectUserImHistoryList(String fromUserId, String toUserId) {
        Page<ImMessageVO> page = imMessageMapper.selectPublicUserImHistoryList(new Page<>(PageUtil.getPageNo(), PageUtil.getPageSize()),
                fromUserId, toUserId);
        formatCreateTime(page);
        return ResponseResult.success(page);
    }

    @Override
    public ResponseResult selectRoomList() {
        List<ImRoomListVO> list = new ArrayList<>();
        List<ImRoom> imRooms = imRoomMapper.selectList(new LambdaQueryWrapper<ImRoom>().eq(ImRoom::getFromUserId, StpUtil.getLoginIdAsString())
                .orderByDesc(ImRoom::getCreateTime));
        for (ImRoom imRoom : imRooms) {
            String toUserId = imRoom.getToUserId();
            UserInfoVO userInfoVO = userMapper.selectInfoByUserId(toUserId);
            ImRoomListVO vo = ImRoomListVO.builder().id(imRoom.getId()).receiveId(toUserId).nickname(userInfoVO.getNickname())
                    .avatar(userInfoVO.getAvatar()).createTimeStr(RelativeDateFormat.format(imRoom.getCreateTime())).build();
            int readNum = imMessageMapper.selectListReadByUserId(toUserId, StpUtil.getLoginIdAsString());
            vo.setReadNum(readNum);
            list.add(vo);
        }
        return ResponseResult.success(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult addRoom(String toUserId) {
        String fromUserId = StpUtil.getLoginIdAsString();
        if (StringUtils.isBlank(toUserId)) {
            throw new BusinessException("Please choose an user!");
        }
        if (toUserId.equals(fromUserId)) {
            throw new BusinessException("Can not chat with your self!");
        }
        ImRoom imRoom = imRoomMapper.selectOne(new LambdaQueryWrapper<ImRoom>().eq(ImRoom::getFromUserId, fromUserId)
                .eq(ImRoom::getToUserId, toUserId));
        if (imRoom != null) {
            return ResponseResult.success();
        }
        imRoom = ImRoom.builder().type(1).fromUserId(fromUserId).toUserId(toUserId).build();
        imRoomMapper.insert(imRoom);
        UserInfoVO userInfoVO = userMapper.selectInfoByUserId(toUserId);
        ImRoomListVO vo = ImRoomListVO.builder().id(imRoom.getId()).receiveId(toUserId).nickname(userInfoVO.getNickname())
                .avatar(userInfoVO.getAvatar()).createTimeStr(RelativeDateFormat.format(imRoom.getCreateTime())).build();
        return ResponseResult.success(vo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult read(String userId) {
        imMessageMapper.updateRead(userId, StpUtil.getLoginIdAsString());
        return ResponseResult.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult deleteRoom(String roomId) {
        imRoomMapper.deleteById(roomId);
        return ResponseResult.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult chat(ImMessageVO obj) {
        Matcher matcher = pattern.matcher(obj.getContent());
        String content = obj.getContent();
        String url = null;
        try {
            while (matcher.find()) {
                url = matcher.group();
                if (url.contains("res.wx.qq.com") || url.contains("npm.elemecdn.com")) {
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
        webSocketInfoService.chat(obj);
        return ResponseResult.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult withdraw(ImMessageVO message) {
        ImMessage entity = imMessageMapper.selectById(message.getId());
        if (DateUtil.getDiffDateToMinutes(entity.getCreateTime(), DateUtil.getNowDate()) >= 2) {
            throw new BusinessException("recall message failed, message sent over 2 minutes!");
        }
        if (!entity.getFromUserId().equals(StpUtil.getLoginIdAsString())) {
            throw new BusinessException("Can only recall your own message!");
        }
        ImMessage imMessage = BeanCopyUtil.copyObject(message, ImMessage.class);
        imMessage.setIp(IpUtil.getIp());
        imMessage.setIpSource(IpUtil.getIp2region(imMessage.getIp()));
        imMessageMapper.updateById(imMessage);
        webSocketInfoService.chat(message);
        return ResponseResult.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult getMessageNotice(Integer type) {
        Page<ImMessageVO> page = imMessageMapper.getMessageNotice(new Page<>(PageUtil.getPageNo(), PageUtil.getPageSize()),
                StpUtil.getLoginIdAsString(), type);
        page.getRecords().forEach(item -> {
            item.setCreateTimeStr(RelativeDateFormat.format(item.getCreateTime()));
        });
        ImMessage message = ImMessage.builder().isRead(1).build();
        imMessageMapper.update(message, new LambdaQueryWrapper<ImMessage>().eq(ImMessage::getToUserId, StpUtil.getLoginIdAsString())
                .eq(ImMessage::getNoticeType, type));
        return ResponseResult.success(page);
    }

    @Override
    public ResponseResult getNewSystemNotice() {
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
        return ResponseResult.success(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult deleteByNoticeType(String id, Integer type) {
        if (StringUtils.isNotBlank(id)) {
            imMessageMapper.deleteById(id);
            return ResponseResult.success();
        }
        imMessageMapper.delete(new LambdaQueryWrapper<ImMessage>().eq(ImMessage::getToUserId, StpUtil.getLoginIdAsString())
                .eq(ImMessage::getNoticeType, type));
        return ResponseResult.success();
    }

    @Override
    public ResponseResult getMessageNoticeApplet(Integer type) {
        Page<ImMessageVO> page = imMessageMapper.getMessageNotice(new Page<>(PageUtil.getPageNo(), PageUtil.getPageSize()),
                StpUtil.getLoginIdAsString(), type);
        page.getRecords().forEach(item -> item.setCreateTimeStr(RelativeDateFormat.format(item.getCreateTime())));
        return ResponseResult.success(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult markReadMessageNoticeApplet(String id) {
        if (StringUtils.isNotBlank(id)) {
            imMessageMapper.updateById(ImMessage.builder().id(id).isRead(YesOrNoEnum.YES.getCode()).build());
            return ResponseResult.success();
        }
        imMessageMapper.update(ImMessage.builder().isRead(YesOrNoEnum.YES.getCode()).build(),new LambdaQueryWrapper<ImMessage>()
                .eq(ImMessage::getToUserId, StpUtil.getLoginIdAsString()));
        return ResponseResult.success();
    }

    private void formatCreateTime(Page<ImMessageVO> page) {
        page.getRecords().forEach(item -> item.setCreateTimeStr(RelativeDateFormat.format(item.getCreateTime())));
    }
}
