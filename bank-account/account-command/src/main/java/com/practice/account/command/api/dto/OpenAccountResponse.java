package com.practice.account.command.api.dto;

import com.practice.account.common.dto.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OpenAccountResponse extends BaseResponse {
    private String id;

    public OpenAccountResponse(String message, String id){
        super(message);
        this.id = id;
    }
}
