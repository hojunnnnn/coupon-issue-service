package com.hojunnnnn.coupon.domain

import java.time.LocalDateTime

@JvmInline
value class CouponId(
    val value: Long,
) {
    companion object {
        fun generate(): CouponId = CouponId(0L) // Auto-increment will assign the actual ID
    }

    init {
        require(value >= 0) { "Coupon Id must be non-negative" }
    }
}

@JvmInline
value class CouponName(
    val value: String,
) {
    init {
        require(value.isNotBlank()) { "Coupon name cannot be blank" }
    }
}

@JvmInline
value class CouponQuantity(
    val value: Int,
) {
    init {
        require(value >= 0) { "Coupon quantity must be positive" }
    }
}

@JvmInline
value class CouponExpirationDays(
    val value: LocalDateTime
) {
    companion object {
        private const val DEFAULT_EXPIRATION_DAYS = 7L
        fun generate(): CouponExpirationDays = CouponExpirationDays(LocalDateTime.now().plusDays(DEFAULT_EXPIRATION_DAYS))
    }
    init {
        require(value.isAfter(LocalDateTime.now())) { "Coupon expiration date must be in the future" }
    }
}

