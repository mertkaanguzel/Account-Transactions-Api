package com.mkg.account.service;

import com.mkg.account.dto.AccountDto;
import com.mkg.account.dto.CreateAccountDto;
import com.mkg.account.dto.converter.AccountDtoConverter;
import com.mkg.account.model.Account;
import com.mkg.account.model.Customer;
import com.mkg.account.model.Transaction;
import com.mkg.account.model.TransactionType;
import com.mkg.account.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;

@Service
public class AccountService {
    public final AccountRepository accountRepository;
    public final AccountDtoConverter accountDtoConverter;
    public final CustomerService customerService;
    public final Clock clock;

    public AccountService(AccountRepository accountRepository, AccountDtoConverter accountDtoConverter, CustomerService customerService, Clock clock) {
        this.accountRepository = accountRepository;
        this.accountDtoConverter = accountDtoConverter;
        this.customerService = customerService;
        this.clock = clock;
    }

    public AccountDto createAccount(CreateAccountDto createAccountDto) {
        Customer customer = customerService.findCustomerById(createAccountDto.getId());

        Account account = new Account(
                customer,
                createAccountDto.getInitialCredit(),
                getLocalDateTimeNow()
        );

        if (createAccountDto.getInitialCredit().compareTo(BigDecimal.ZERO) > 0) {
            Transaction transaction = new Transaction(
                    createAccountDto.getInitialCredit(),
                    getLocalDateTimeNow(),
                    account
            );

            account.getTransaction().add(transaction);
        }

        return accountDtoConverter.convert(accountRepository.save(account));
    }

    private LocalDateTime getLocalDateTimeNow() {
        Instant instant = clock.instant();
        return LocalDateTime.ofInstant(
                instant,
                Clock.systemDefaultZone().getZone()
        );
    }
}
