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
        require(value >= 0) { "Coupon quantity cannot be negative" }
    }

    fun hasRemaining(): Boolean = value > 0

    fun decrease(): CouponQuantity {
        require(hasRemaining()) { "No remaining coupons to issue" }
        return CouponQuantity(value - 1)
    }

}

@JvmInline
value class CouponExpirationDays(
    val value: LocalDateTime,
) {
    companion object {
        private const val DEFAULT_EXPIRATION_DAYS = 7L

        fun generate(): CouponExpirationDays = CouponExpirationDays(LocalDateTime.now().plusDays(DEFAULT_EXPIRATION_DAYS))
    }

    fun isExpired(now: LocalDateTime = LocalDateTime.now()): Boolean =
        value.isBefore(now)
}
