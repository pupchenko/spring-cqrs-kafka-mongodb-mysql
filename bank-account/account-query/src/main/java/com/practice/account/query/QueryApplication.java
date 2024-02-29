package com.practice.account.query;

import com.practice.account.query.api.queries.*;
import com.practice.cqrs.core.infrastructure.QueryDispatcher;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
    public void registerHandler(){
        queryDispatcher.registerHandler(FindAllAccountsQuery.class, queryHandler::handle);
        queryDispatcher.registerHandler(FindAccountByIdQuery.class, queryHandler::handle);
        queryDispatcher.registerHandler(FindAccountHolderQuery.class, queryHandler::handle);
        queryDispatcher.registerHandler(FindAccountWithBalanceQuery.class, queryHandler::handle);
    }
}
