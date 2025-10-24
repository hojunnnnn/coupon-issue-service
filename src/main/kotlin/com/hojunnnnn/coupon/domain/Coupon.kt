package com.hojunnnnn.coupon.domain

import java.time.LocalDateTime

data class Coupon(
    val id: CouponId,

    val name: CouponName,

    var quantity: CouponQuantity,

    val createdDateTime: LocalDateTime = LocalDateTime.now(),

    val expiredDateTime: CouponExpirationDays = CouponExpirationDays.generate()
) {

    companion object {
        fun create(
            name: String,
            quantity: Int,
            expiredDateTime: LocalDateTime = CouponExpirationDays.generate().value,
        ): Coupon {
            return Coupon(
                id = CouponId.generate(),
                name = CouponName(name),
                quantity = CouponQuantity(quantity),
                expiredDateTime = CouponExpirationDays(expiredDateTime)
            )
        }
    }

    fun decreaseQuantity() {
        quantity = CouponQuantity(quantity.value - 1)
    }
}
