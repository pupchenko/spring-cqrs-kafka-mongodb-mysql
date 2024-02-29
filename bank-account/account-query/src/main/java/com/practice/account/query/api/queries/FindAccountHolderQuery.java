package com.practice.account.query.api.queries;

import com.practice.cqrs.core.queries.BaseQuery;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FindAccountHolderQuery extends BaseQuery {
    private String accountHolder;
}
