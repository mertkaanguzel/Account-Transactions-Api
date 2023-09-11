package com.mkg.account.controller;

import com.mkg.account.IntegrationTestSupport;
import com.mkg.account.dto.CreateAccountDto;
import com.mkg.account.dto.CustomerDto;
import com.mkg.account.exception.CustomerNotFoundException;
import com.mkg.account.model.Customer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "server-port=0",
                "command.line.runner.enabled=false"
        }
)

@ExtendWith(SpringExtension.class)
@DirtiesContext
class CustomerControllerTest extends IntegrationTestSupport {

    @Test
    void testGetCustomerById_whenCustomerIdExists_shouldReturnCustomerDto() throws Exception {
        Customer customer = customerRepository.save(new Customer("id", "name", "surname", Set.of()));
        accountService.createAccount(new CreateAccountDto(Objects.requireNonNull(customer.getId()), new BigDecimal(100)));

        CustomerDto customerDto = customerDtoConverter.convert(
                customerRepository.findById(customer.getId())
                        .orElseThrow(() -> new CustomerNotFoundException("Customer not found with given id: " + customer.getId()))
        );

        this.mockMvc.perform(get(CUSTOMER_API_ENDPOINT + "/" + customer.getId()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(customerDto), false))
                .andReturn();

    }

    @Test
    void testGetCustomerById_whenCustomerIdDoesNotExist_shouldReturn404NotFound() throws Exception {
        this.mockMvc.perform(get(CUSTOMER_API_ENDPOINT + "/" + "id"))
                .andExpect(status().isNotFound())
                .andReturn();
    }
}