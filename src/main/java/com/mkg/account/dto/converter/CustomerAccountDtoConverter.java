package com.mkg.account.dto.converter;

import com.mkg.account.dto.CustomerAccountDto;
import com.mkg.account.model.Account;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class CustomerAccountDtoConverter {
    private final TransactionDtoConverter transactionDtoConverter;

    public CustomerAccountDtoConverter(TransactionDtoConverter transactionDtoConverter) {
        this.transactionDtoConverter = transactionDtoConverter;
    }

    public CustomerAccountDto convert(Account from) {
        return new CustomerAccountDto(
                Objects.requireNonNull(from.getId()),
                from.getBalance(),
                from.getCreationDate(),
                from.getTransaction()
                        .stream().map(transactionDtoConverter::convert)
                        .collect(Collectors.toSet())
        );
    }
}
