package com.practice.account.query.api.queries;

import com.practice.account.query.api.dto.EqualityType;
import com.practice.cqrs.core.queries.BaseQuery;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FindAccountWithBalanceQuery extends BaseQuery {
    private EqualityType equalityType;
    private double balance;
}
