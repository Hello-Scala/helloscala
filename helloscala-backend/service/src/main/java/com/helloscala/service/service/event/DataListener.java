package com.helloscala.service.service.event;

import com.alibaba.fastjson.JSONObject;
import com.helloscala.service.entity.ArticleElastic;
import com.helloscala.service.entity.FriendLink;
import com.helloscala.service.entity.Resource;
import com.helloscala.service.esmapper.EasyesMapper;
import com.helloscala.service.mapper.ResourceMapper;
import com.helloscala.service.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DataListener {
    private final EasyesMapper easyESMapper;
    private final ResourceMapper resourceMapper;
    private final EmailService emailService;


    @Async("threadPoolTaskExecutor")
    @EventListener
    public void handleDataSyncEvent(DataEvent<?, ?> event) {
        if (event == null || event.getEntity() == null) {
            log.error("Event and Event data must not be null.");
            return;
        }
        log.info("event={}, entity={}", event.getEventEnum(), JSONObject.toJSONString(event.getEntity()));
        handleEvent(event);
    }

    private void handleEvent(DataEvent<?, ?> event) {
        try {
            switch (event.getEventEnum()) {
                case ES_ADD_ARTICLE:
                case ES_UPDATE_ARTICLE:
                    updateOrAdd((ArticleElastic) event.getEntity());
                    break;
                case ES_DELETE_ARTICLE:
                    @SuppressWarnings("unchecked")
                    List<Long> ids = (List<Long>) event.getEntity();
                    easyESMapper.deleteBatchIds(ids);
                    break;
                case RESOURCE_ADD:
                    resourceMapper.insert((Resource) event.getEntity());
                    break;
                case EMAIL_SEND:
                    emailService.sendFriendEmail((FriendLink) event.getEntity());
                    break;
                default:
                    log.warn("Unhandled event type: " + event.getEventEnum());
                    break;
            }
        } catch (Exception e) {
            log.error("Error handling event: " + event.getEventEnum(), e);
        }
    }

    private void updateOrAdd(ArticleElastic articleElastic) {
        if (articleElastic.getId() == null) {
            easyESMapper.insert(articleElastic);
        } else {
            easyESMapper.updateById(articleElastic);
        }
    }
}
