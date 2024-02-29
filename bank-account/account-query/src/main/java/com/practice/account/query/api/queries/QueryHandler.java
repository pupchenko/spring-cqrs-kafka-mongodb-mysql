package com.practice.account.query.api.queries;

import com.practice.cqrs.core.domain.BaseEntity;

import java.util.List;

public interface QueryHandler {
    List<BaseEntity> handle(FindAllAccountsQuery query);
    List<BaseEntity> handle(FindAccountByIdQuery query);
    List<BaseEntity> handle(FindAccountHolderQuery query);
    List<BaseEntity> handle(FindAccountWithBalanceQuery query);
}
