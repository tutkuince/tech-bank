package com.techbank.cqrs.core.query;

import com.techbank.cqrs.core.event.BaseEvent;

import java.util.List;

@FunctionalInterface
public interface QueryHandlerMethod<T extends BaseQuery> {
    List<BaseEvent> handle(T query);
}
