package com.helloscala.service.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.helloscala.service.entity.FriendLink;
import com.helloscala.service.mapper.FriendLinkMapper;
import com.helloscala.service.service.EmailService;
import com.helloscala.common.vo.friendLink.ApiFriendLinkVO;
import com.helloscala.common.web.exception.ConflictException;
import com.helloscala.web.service.ApiFriendLinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.helloscala.service.enums.FriendLinkEnum.APPLY;
import static com.helloscala.service.enums.FriendLinkEnum.UP;

@Service
@RequiredArgsConstructor
public class ApiFriendLinkServiceImpl implements ApiFriendLinkService {

    private final FriendLinkMapper friendLinkMapper;

    private final EmailService emailService;

    @Override
    public List<ApiFriendLinkVO> selectFriendLinkList() {
        return friendLinkMapper.selectLinkList(UP.code);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addFriendLink(FriendLink friendLink) {
        FriendLink entity = friendLinkMapper.selectOne(new LambdaQueryWrapper<FriendLink>()
                .eq(FriendLink::getUrl,friendLink.getUrl()));
        if (entity != null){
            throw new ConflictException("Friend link exist, please leave notes for modification!");
        }
        friendLink.setStatus(APPLY.getCode());
        friendLinkMapper.insert(friendLink);
        emailService.emailNoticeMe("New friend link apply!","New friend link apply ("+friendLink.getUrl()+"), please got to verify!!!");
    }
}
