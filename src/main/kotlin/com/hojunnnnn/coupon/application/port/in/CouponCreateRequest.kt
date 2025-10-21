package com.hojunnnnn.coupon.application.port.`in`

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Positive

data class CouponCreateRequest(
    @field:NotBlank
    @field:Pattern(regexp = "^\\S+$")
    val name: String,

    @field:Positive
    val quantity: Int,
)