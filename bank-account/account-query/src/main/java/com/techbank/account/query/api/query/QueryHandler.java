package com.techbank.account.query.api.query;

import com.techbank.cqrs.core.domain.BaseEntity;
import com.techbank.cqrs.core.event.BaseEvent;

import java.util.List;

public interface QueryHandler {
    List<BaseEntity> handle(FindAllAccountsQuery query);
    List<BaseEntity> handle(FindAccountWithBalanceQuery query);
    List<BaseEntity> handle(FindAccountByIdQuery query);
    List<BaseEntity> handle(FindAccountByHolderQuery query);
}
