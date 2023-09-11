package com.mkg.account.service;

import com.mkg.account.dto.CustomerDto;
import com.mkg.account.dto.converter.CustomerDtoConverter;
import com.mkg.account.exception.CustomerNotFoundException;
import com.mkg.account.model.Customer;
import com.mkg.account.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerDtoConverter customerDtoConverter;

    public CustomerService(CustomerRepository customerRepository, CustomerDtoConverter customerDtoConverter) {
        this.customerRepository = customerRepository;
        this.customerDtoConverter = customerDtoConverter;
    }

    protected Customer findCustomerById(String id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with given id: " + id));
    }

    public CustomerDto getCustomerById(String id) {
        return customerDtoConverter.convert(findCustomerById(id));
    }

    public List<CustomerDto> getAllCustomer() {
        return customerRepository.findAll()
                .stream().map(customerDtoConverter::convert)
                .collect(Collectors.toList());
    }
}
