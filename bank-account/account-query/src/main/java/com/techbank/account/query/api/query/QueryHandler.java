package com.techbank.account.query.api.query;

import com.techbank.cqrs.core.event.BaseEvent;

import java.util.List;

public interface QueryHandler {
    List<BaseEvent> handle(FindAllAccountsQuery query);
    List<BaseEvent> handle(FindAccountWithBalanceQuery query);
    List<BaseEvent> handle(FindAccountByIdQuery query);
    List<BaseEvent> handle(FindAccountByHolderQuery query);
}
