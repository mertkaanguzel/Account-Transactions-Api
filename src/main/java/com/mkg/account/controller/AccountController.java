package com.mkg.account.controller;

import com.mkg.account.dto.AccountDto;
import com.mkg.account.dto.CreateAccountDto;
import com.mkg.account.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/account")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<AccountDto> createAccount(@Valid @RequestBody CreateAccountDto createAccountDto) {
        return  ResponseEntity.ok(accountService.createAccount(createAccountDto));
    }
}
