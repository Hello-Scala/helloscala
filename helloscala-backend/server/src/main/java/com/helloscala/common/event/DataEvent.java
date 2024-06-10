package com.helloscala.common.event;

import com.helloscala.common.enums.DataEventEnum;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class DataEvent<S, T> extends ApplicationEvent {
    private final Class<S> sClass;
    private final T entity;
    private final Class<T> tClass;
    private final DataEventEnum eventEnum;

    @SuppressWarnings("unchecked")
    public DataEvent(S source, DataEventEnum eventEnum, T data) {
        super(source);
        sClass = (Class<S>) source.getClass();
        this.eventEnum = eventEnum;
        this.tClass = (Class<T>) data.getClass();
        this.entity = data;
    }
}
