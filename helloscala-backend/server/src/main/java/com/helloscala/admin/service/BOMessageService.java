package com.helloscala.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.helloscala.admin.controller.view.BOMessageView;
import com.helloscala.common.utils.PageHelper;
import com.helloscala.common.utils.PageUtil;
import com.helloscala.service.service.MessageService;
import com.helloscala.service.web.view.MessageView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class BOMessageService {
    private final MessageService messageService;

    public Page<BOMessageView> listByPage(String name) {
        Page<?> page = PageUtil.getPage();
        Page<MessageView> messageViewPage = messageService.selectMessagePage(page, name);
        return PageHelper.convertTo(messageViewPage, message -> {
            BOMessageView messageView = new BOMessageView();
            messageView.setId(message.getId());
            messageView.setContent(message.getContent());
            messageView.setNickname(message.getNickname());
            messageView.setAvatar(message.getAvatar());
            messageView.setIpAddress(message.getIpAddress());
            messageView.setTime(message.getTime());
            messageView.setIpSource(message.getIpSource());
            messageView.setStatus(message.getStatus());
            messageView.setCreateTime(message.getCreateTime());
            return messageView;
        });
    }

    public void bulkDelete(String userId, Set<String> ids) {
        messageService.deleteMessage(ids);
        log.info("userId={}, deleted message ids=[{}]", userId, String.join(",", ids));
    }

}
