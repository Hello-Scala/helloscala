package com.helloscala.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.helloscala.common.ResponseResult;
import com.helloscala.entity.FriendLink;
import com.helloscala.exception.BusinessException;
import com.helloscala.mapper.FriendLinkMapper;
import com.helloscala.service.ApiFriendLinkService;
import com.helloscala.service.EmailService;
import com.helloscala.vo.friendLink.ApiFriendLinkVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.helloscala.enums.FriendLinkEnum.APPLY;
import static com.helloscala.enums.FriendLinkEnum.UP;

@Service
@RequiredArgsConstructor
public class ApiFriendLinkServiceImpl implements ApiFriendLinkService {

    private final FriendLinkMapper friendLinkMapper;

    private final EmailService emailService;

    /**
     * 友链列表
     * @return
     */
    @Override
    public ResponseResult selectFriendLinkList() {
        List<ApiFriendLinkVO> list = friendLinkMapper.selectLinkList(UP.code);
        return ResponseResult.success(list);
    }

    /**
     * 申请友链
     * @param friendLink
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult addFriendLink(FriendLink friendLink) {

        //如果已经申请过友链 再次接入则会进行下架处理 需重新审核
        FriendLink entity = friendLinkMapper.selectOne(new LambdaQueryWrapper<FriendLink>()
                .eq(FriendLink::getUrl,friendLink.getUrl()));
        if (entity != null){
            throw new BusinessException("Friend link exist, please leave notes for modification!");
        }
        friendLink.setStatus(APPLY.getCode());
        friendLinkMapper.insert(friendLink);
        //异步操作邮箱发送
        emailService.emailNoticeMe("友链接入通知","网站有新的友链接入啦("+friendLink.getUrl()+")，快去审核吧!!!");
        return ResponseResult.success();
    }
}
