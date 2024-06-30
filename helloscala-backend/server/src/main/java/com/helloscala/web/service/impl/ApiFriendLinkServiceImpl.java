package com.helloscala.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.helloscala.common.ResponseResult;
import com.helloscala.common.entity.FriendLink;
import com.helloscala.common.mapper.FriendLinkMapper;
import com.helloscala.common.service.EmailService;
import com.helloscala.common.vo.friendLink.ApiFriendLinkVO;
import com.helloscala.common.web.exception.ConflictException;
import com.helloscala.web.service.ApiFriendLinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.helloscala.common.enums.FriendLinkEnum.APPLY;
import static com.helloscala.common.enums.FriendLinkEnum.UP;

@Service
@RequiredArgsConstructor
public class ApiFriendLinkServiceImpl implements ApiFriendLinkService {

    private final FriendLinkMapper friendLinkMapper;

    private final EmailService emailService;

    @Override
    public ResponseResult selectFriendLinkList() {
        List<ApiFriendLinkVO> list = friendLinkMapper.selectLinkList(UP.code);
        return ResponseResult.success(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult addFriendLink(FriendLink friendLink) {
        FriendLink entity = friendLinkMapper.selectOne(new LambdaQueryWrapper<FriendLink>()
                .eq(FriendLink::getUrl,friendLink.getUrl()));
        if (entity != null){
            throw new ConflictException("Friend link exist, please leave notes for modification!");
        }
        friendLink.setStatus(APPLY.getCode());
        friendLinkMapper.insert(friendLink);
        emailService.emailNoticeMe("New friend link apply!","New friend link apply ("+friendLink.getUrl()+"), please got to verify!!!");
        return ResponseResult.success();
    }
}
