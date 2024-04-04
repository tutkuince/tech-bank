package com.techbank.cqrs.core.query;

import com.techbank.cqrs.core.domain.BaseEntity;
import com.techbank.cqrs.core.event.BaseEvent;

import java.util.List;

@FunctionalInterface
public interface QueryHandlerMethod<T extends BaseQuery> {
    List<BaseEntity> handle(T query);
}
