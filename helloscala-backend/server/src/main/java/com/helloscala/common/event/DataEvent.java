package com.helloscala.common.event;

import com.helloscala.common.enums.DataEventEnum;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class DataEvent extends ApplicationEvent {
    private final Object data;
    private final DataEventEnum eventEnum;

    public DataEvent(Object source, DataEventEnum eventEnum, Object data) {
        super(source);
        this.data = data;
        this.eventEnum = eventEnum;
    }
}
