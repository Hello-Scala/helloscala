package com.helloscala.common.event;

import com.helloscala.common.enums.DataEventEnum;
import com.helloscala.common.event.DataEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class DataEventPublisherService {

    private final ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public DataEventPublisherService(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void publishData(DataEventEnum dataEventEnum,Object data) {
        DataEvent event = new DataEvent(this,dataEventEnum, data);
        applicationEventPublisher.publishEvent(event);
    }
}
