package com.mkg.account.dto

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import java.math.BigDecimal


data class CreateAccountDto(
        @field:NotBlank(message = "Customer Id must not be empty")
        val id: String,

        @field:Min(0, message = "Initial Credit value must be non negative value")
        val initialCredit: BigDecimal,
)
