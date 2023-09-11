package com.mkg.account.controller;

import com.mkg.account.IntegrationTestSupport;
import com.mkg.account.dto.CreateAccountDto;
import com.mkg.account.model.Customer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
class AccountControllerTest extends IntegrationTestSupport {

    @Test
    void testCreateAccount_whenCustomerIdExists_shouldCreateAccountAndReturnAccountDto() throws Exception {
        Customer customer = customerRepository.save(new Customer("id", "name", "surname", Set.of()));
        CreateAccountDto createAccountDto = new CreateAccountDto(Objects.requireNonNull(customer.getId()), new BigDecimal(100));

        this.mockMvc.perform(
                post(ACCOUNT_API_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(createAccountDto))
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.balance", is(100)))
                .andExpect(jsonPath("$.customer.id", is(customer.getId())))
                .andExpect(jsonPath("$.customer.name", is(customer.getName())))
                .andExpect(jsonPath("$.customer.surname", is(customer.getSurname())))
                .andExpect(jsonPath("$.transactions", hasSize(1)));
    }

    @Test
    void testCreateAccount_whenCustomerIdDoesNotExist_shouldReturn404NotFound() throws Exception {
        CreateAccountDto createAccountDto = new CreateAccountDto("id", new BigDecimal(100));

        this.mockMvc.perform(
                        post(ACCOUNT_API_ENDPOINT)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(createAccountDto))
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateAccount_whenRequestHasNoCustomerId_shouldReturn400BadRequest() throws Exception {
        CreateAccountDto createAccountDto = new CreateAccountDto("", new BigDecimal(100));

        this.mockMvc.perform(
                        post(ACCOUNT_API_ENDPOINT)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(createAccountDto))
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testCreateAccount_whenRequestHasNegativeInitialCreditValue_shouldReturn400BadRequest() throws Exception {
        CreateAccountDto createAccountDto = new CreateAccountDto("id", new BigDecimal(-100));

        this.mockMvc.perform(
                        post(ACCOUNT_API_ENDPOINT)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(createAccountDto))
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}