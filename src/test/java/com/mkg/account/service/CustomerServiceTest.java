package com.mkg.account.service;

import com.mkg.account.dto.CustomerDto;
import com.mkg.account.dto.converter.CustomerDtoConverter;
import com.mkg.account.exception.CustomerNotFoundException;
import com.mkg.account.model.Customer;
import com.mkg.account.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CustomerServiceTest {
    private CustomerService service;
    private CustomerRepository customerRepository;
    private CustomerDtoConverter customerDtoConverter;

    private final Customer customer = new Customer("id", "name", "surname", Set.of());
    private final CustomerDto customerDto = new CustomerDto("id", "name", "surname", Set.of());

    @BeforeEach
    public void setUp() {
        customerRepository = mock(CustomerRepository.class);
        customerDtoConverter = mock(CustomerDtoConverter.class);
        service = new CustomerService(customerRepository, customerDtoConverter);
    }

    @Test
    void testFindCustomerById_whenCustomerIdExists_shouldReturnCustomer() {
        when(customerRepository.findById("id")).thenReturn(Optional.of(customer));

        Customer result = service.findCustomerById("id");
        assertEquals(customer, result);
    }

    @Test
    void testFindCustomerById_whenCustomerIdNotExists_shouldThrowCustomerNotFoundException() {
        when(customerRepository.findById("id")).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> service.findCustomerById("id"));
    }

    @Test
    void testGetCustomerById_whenCustomerIdExists_shouldReturnCustomerDto() {
        when(customerRepository.findById("id")).thenReturn(Optional.of(customer));
        when(customerDtoConverter.convert(customer)).thenReturn(customerDto);

        CustomerDto result = service.getCustomerById("id");
        assertEquals(customerDto, result);
    }

    @Test
    void testGetCustomerById_whenCustomerIdNotExists_shouldThrowCustomerNotFoundException() {
        when(customerRepository.findById("id")).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> service.getCustomerById("id"));
        Mockito.verifyNoInteractions(customerDtoConverter);
    }
}