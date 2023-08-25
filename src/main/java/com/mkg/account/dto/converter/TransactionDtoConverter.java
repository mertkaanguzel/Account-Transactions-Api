package com.mkg.account.dto.converter;

import com.mkg.account.dto.TransactionDto;
import com.mkg.account.model.Transaction;
import org.springframework.stereotype.Component;

@Component
public class TransactionDtoConverter {
    public TransactionDto convert(Transaction from) {
        return new TransactionDto(
                from.getId(),
                from.getTransactionType(),
                from.getAmount(),
                from.getTransactionDate()
        );
    }
}
