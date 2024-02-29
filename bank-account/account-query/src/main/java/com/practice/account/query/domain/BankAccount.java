package com.practice.account.query.domain;

import com.practice.account.common.dto.AccountType;
import com.practice.cqrs.core.domain.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankAccount extends BaseEntity{
    @Id
    private String id;
    private String accountHolder;
    private Date creationDate;
    @Enumerated(EnumType.STRING)
    private AccountType accountType;
    private double balance;
}
