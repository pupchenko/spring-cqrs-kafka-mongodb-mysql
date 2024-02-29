package com.practice.account.query.api.dto;

import com.practice.account.common.dto.BaseResponse;
import com.practice.account.query.domain.BankAccount;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
public class AccountLookupResponse extends BaseResponse {
    private List<BankAccount> accounts;
    public AccountLookupResponse(String message){
        super(message);
    }
}
