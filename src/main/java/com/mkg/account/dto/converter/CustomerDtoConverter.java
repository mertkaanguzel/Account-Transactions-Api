package com.mkg.account.dto.converter;

import com.mkg.account.dto.CustomerDto;
import com.mkg.account.model.Customer;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class CustomerDtoConverter {
    private final CustomerAccountDtoConverter customerAccountDtoConverter;

    public CustomerDtoConverter(CustomerAccountDtoConverter customerAccountDtoConverter) {
        this.customerAccountDtoConverter = customerAccountDtoConverter;
    }

    public CustomerDto convert(Customer from) {
        return new CustomerDto(
                from.getId(),
                from.getName(),
                from.getSurname(),
                from.getAccounts()
                        .stream().map(customerAccountDtoConverter::convert)
                        .collect(Collectors.toSet())
        );
    }
}
