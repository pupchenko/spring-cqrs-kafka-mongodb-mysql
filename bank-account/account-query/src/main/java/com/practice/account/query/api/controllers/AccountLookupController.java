package com.practice.account.query.api.controllers;

import com.practice.account.query.api.dto.AccountLookupResponse;
import com.practice.account.query.api.dto.EqualityType;
import com.practice.account.query.api.queries.FindAccountByIdQuery;
import com.practice.account.query.api.queries.FindAccountHolderQuery;
import com.practice.account.query.api.queries.FindAccountWithBalanceQuery;
import com.practice.account.query.api.queries.FindAllAccountsQuery;
import com.practice.account.query.domain.BankAccount;
import com.practice.cqrs.core.infrastructure.QueryDispatcher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping(path = "/api/v1/accounts")
public class AccountLookupController {
    private final Logger logger = Logger.getLogger(AccountLookupController.class.getName());

    private final QueryDispatcher queryDispatcher;

    public AccountLookupController(QueryDispatcher queryDispatcher) {
        this.queryDispatcher = queryDispatcher;
    }

    @GetMapping
    public ResponseEntity<AccountLookupResponse> getAllAccounts(){
        try{
            List<BankAccount> accounts = queryDispatcher.send(new FindAllAccountsQuery());
            if (accounts == null || accounts.isEmpty()){
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
            var response = AccountLookupResponse.builder()
                    .accounts(accounts)
                    .message(MessageFormat.format("Successfully returned {0} bank account(s)", accounts.size()))
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            var safeErrorMessage = "Failed to complete get all accounts request";
            logger.log(Level.SEVERE, safeErrorMessage, e);
            return new ResponseEntity<>(new AccountLookupResponse(safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/search", params = {"id", "!accountHolder", "!equalityType",  "!balance"})
    public ResponseEntity<AccountLookupResponse> getAccountById(@RequestParam(name = "id") String id){
        try{
            List<BankAccount> accounts = queryDispatcher.send(new FindAccountByIdQuery(id));
            if (accounts == null || accounts.isEmpty()){
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
            var response = AccountLookupResponse.builder()
                    .accounts(accounts)
                    .message("Successfully returned bank account")
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            var safeErrorMessage = "Failed to complete get account by Id request";
            logger.log(Level.SEVERE, safeErrorMessage, e);
            return new ResponseEntity<>(new AccountLookupResponse(safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/search", params = {"!id", "accountHolder", "!equalityType",  "!balance"})
    public ResponseEntity<AccountLookupResponse> getAccountByHolder(@RequestParam(name = "accountHolder") String accountHolder){
        try{
            List<BankAccount> accounts = queryDispatcher.send(new FindAccountHolderQuery(accountHolder));
            if (accounts == null || accounts.isEmpty()){
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
            var response = AccountLookupResponse.builder()
                    .accounts(accounts)
                    .message("Successfully returned bank account")
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            var safeErrorMessage = "Failed to complete get account by account holder request";
            logger.log(Level.SEVERE, safeErrorMessage, e);
            return new ResponseEntity<>(new AccountLookupResponse(safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/search", params = {"!id", "!accountHolder", "equalityType",  "balance"})
    public ResponseEntity<AccountLookupResponse> getAccountByHolder(@RequestParam(name = "equalityType") EqualityType equalityType, @RequestParam(name = "balance") double balance){
        try{
            List<BankAccount> accounts = queryDispatcher.send(new FindAccountWithBalanceQuery(equalityType, balance));
            if (accounts == null || accounts.isEmpty()){
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
            var response = AccountLookupResponse.builder()
                    .accounts(accounts)
                    .message("Successfully returned bank account(s)")
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            var safeErrorMessage = "Failed to complete get account by equality type and balance request";
            logger.log(Level.SEVERE, safeErrorMessage, e);
            return new ResponseEntity<>(new AccountLookupResponse(safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
