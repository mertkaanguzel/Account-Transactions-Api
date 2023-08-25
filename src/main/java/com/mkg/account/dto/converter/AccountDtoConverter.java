package com.mkg.account.dto.converter;

import com.mkg.account.dto.AccountDto;
import com.mkg.account.model.Account;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class AccountDtoConverter {
    private final AccountCustomerDtoConverter accountCustomerDtoConverter;
    private final TransactionDtoConverter transactionDtoConverter;

    public AccountDtoConverter(AccountCustomerDtoConverter accountCustomerDtoConverter, TransactionDtoConverter transactionDtoConverter) {
        this.accountCustomerDtoConverter = accountCustomerDtoConverter;
        this.transactionDtoConverter = transactionDtoConverter;
    }

    public AccountDto convert(Account from) {
        return new AccountDto(
                from.getId(),
                from.getBalance(),
                from.getCreationDate(),
                accountCustomerDtoConverter.convert(Optional.ofNullable(from.getCustomer())),
                Objects.requireNonNull(from.getTransaction())
                        .stream().map(transactionDtoConverter::convert)
                        .collect(Collectors.toSet())

        );
    }
}
