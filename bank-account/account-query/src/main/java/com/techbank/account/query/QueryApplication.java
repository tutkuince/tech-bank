package com.techbank.account.query;

import com.techbank.account.query.api.query.*;
import com.techbank.cqrs.core.infrastructure.QueryDispatcher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class QueryApplication {

    private final QueryDispatcher queryDispatcher;
    private final QueryHandler queryHandler;

    public QueryApplication(QueryDispatcher queryDispatcher, QueryHandler queryHandler) {
        this.queryDispatcher = queryDispatcher;
        this.queryHandler = queryHandler;
    }

    public static void main(String[] args) {
        SpringApplication.run(QueryApplication.class, args);
    }

    @PostConstruct
    public void registerHandlers() {
        queryDispatcher.registerHandler(FindAllAccountsQuery.class, queryHandler::handle);
        queryDispatcher.registerHandler(FindAccountWithBalanceQuery.class, queryHandler::handle);
        queryDispatcher.registerHandler(FindAccountByIdQuery.class, queryHandler::handle);
        queryDispatcher.registerHandler(FindAccountByHolderQuery.class, queryHandler::handle);
    }
}
