package com.mkg.account.dto.converter;

import com.mkg.account.dto.AccountCustomerDto;
import com.mkg.account.model.Customer;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AccountCustomerDtoConverter {
    public AccountCustomerDto convert(Optional<Customer> from) {
        return from.map(f -> new AccountCustomerDto(
                f.getId(),
                f.getName(),
                f.getSurname()
        )).orElse(null);
    }
}
