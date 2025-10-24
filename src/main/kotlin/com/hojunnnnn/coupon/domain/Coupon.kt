package com.hojunnnnn.coupon.domain

import java.time.LocalDateTime

data class Coupon(
    val id: CouponId,

    val name: CouponName,

    var quantity: CouponQuantity,

    val createdDateTime: LocalDateTime = LocalDateTime.now(),

    val expiredDateTime: LocalDateTime = LocalDateTime.now().plusDays(7)
) {


    init {
        require(quantity.value >= 0) { "쿠폰 수량은 0 이상이어야 합니다." }
        require(LocalDateTime.now().isBefore(expiredDateTime)) { "쿠폰 만료일은 현재 시간 이후여야 합니다." }
    }


    companion object {
        fun create(
            name: String,
            quantity: Int,
            expiredDateTime: LocalDateTime = LocalDateTime.now().plusDays(7),
        ): Coupon {
            return Coupon(
                id = CouponId.generate(),
                name = CouponName(name),
                quantity = CouponQuantity(quantity),
                expiredDateTime = expiredDateTime
            )
        }
    }

    fun decreaseQuantity() {
        quantity = CouponQuantity(quantity.value - 1)
    }
}
