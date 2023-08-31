package com.mkg.account.service;

import com.mkg.account.dto.AccountCustomerDto;
import com.mkg.account.dto.AccountDto;
import com.mkg.account.dto.CreateAccountDto;
import com.mkg.account.dto.TransactionDto;
import com.mkg.account.dto.converter.AccountDtoConverter;
import com.mkg.account.exception.CustomerNotFoundException;
import com.mkg.account.model.Account;
import com.mkg.account.model.Customer;
import com.mkg.account.model.Transaction;
import com.mkg.account.model.TransactionType;
import com.mkg.account.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Clock;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static com.mkg.account.TestSupport.getInstant;
import static com.mkg.account.TestSupport.getLocalDateTime;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountServiceTest {

    private AccountService accountService;

    private CustomerService customerService;
    private AccountRepository accountRepository;
    private AccountDtoConverter accountDtoConverter;

    private final Customer customer = new Customer("id", "name", "surname", Set.of());
    private final AccountCustomerDto accountCustomerDto = new AccountCustomerDto("", "name", "surname");

    @BeforeEach
    void setUp() {
        accountRepository = mock(AccountRepository.class);
        accountDtoConverter = mock(AccountDtoConverter.class);
        customerService = mock(CustomerService.class);

        Clock clock = mock(Clock.class);
        when(clock.instant()).thenReturn(getInstant());
        when(clock.getZone()).thenReturn(Clock.systemDefaultZone().getZone());

        accountService = new AccountService(accountRepository, accountDtoConverter, customerService, clock);

    }

    @Test
    void testCreateAccount_whenCustomerIdExistsAndInitialCreditMoreThanZero_shouldCreateAccountWithTransaction() {
        CreateAccountDto createAccountDto = new CreateAccountDto("id", new BigDecimal(100));

        Account account = new Account("", createAccountDto.getInitialCredit(), getLocalDateTime(), customer, new HashSet<>());
        Transaction transaction = new Transaction(null, TransactionType.INITIAL, createAccountDto.getInitialCredit(), getLocalDateTime(), account);
        account.getTransaction().add(transaction);

        TransactionDto transactionDto = new TransactionDto("", TransactionType.INITIAL, new BigDecimal(100), getLocalDateTime());
        AccountDto accountDto = new AccountDto("id", new BigDecimal(100), getLocalDateTime(), accountCustomerDto, Set.of(transactionDto));

        when(customerService.findCustomerById("id")).thenReturn(customer);
        when(accountRepository.save(account)).thenReturn(account);
        when(accountDtoConverter.convert(account)).thenReturn(accountDto);

        AccountDto result = accountService.createAccount(createAccountDto);
        assertEquals(accountDto, result);
    }

    @Test
    void testCreateAccount_whenCustomerIdExistsAndInitialCreditIsZero_shouldCreateAccountWithoutTransaction() {
        CreateAccountDto createAccountDto = new CreateAccountDto("id", new BigDecimal(0));

        Account account = new Account("", createAccountDto.getInitialCredit(), getLocalDateTime(), customer, new HashSet<>());

        AccountDto accountDto = new AccountDto("id", new BigDecimal(0), getLocalDateTime(), accountCustomerDto, Set.of());

        when(customerService.findCustomerById("id")).thenReturn(customer);
        when(accountRepository.save(account)).thenReturn(account);
        when(accountDtoConverter.convert(account)).thenReturn(accountDto);

        AccountDto result = accountService.createAccount(createAccountDto);
        assertEquals(accountDto, result);
    }

    @Test
    void testCreateAccount_whenCustomerIdNotExists_shouldThrowCustomerNotFoundException() {
        CreateAccountDto createAccountDto = new CreateAccountDto("id", new BigDecimal(0));

        when(customerService.findCustomerById(createAccountDto.getId())).thenThrow(new CustomerNotFoundException("Customer not found with given id" + createAccountDto.getId()));

        assertThrows(CustomerNotFoundException.class,
                () -> accountService.createAccount(createAccountDto)
        );
        verifyNoInteractions(accountRepository);
        verifyNoInteractions(accountDtoConverter);
    }


}