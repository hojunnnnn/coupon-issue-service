package com.hojunnnnn.coupon.domain

import com.hojunnnnn.coupon.errors.CouponExpiredException
import com.hojunnnnn.coupon.errors.CouponOutOfStockException
import java.time.LocalDateTime

data class Coupon(
    val id: CouponId = CouponId.generate(),
    val name: CouponName,
    var quantity: CouponQuantity,
    val createdDateTime: LocalDateTime = LocalDateTime.now(),
    val expiredDateTime: CouponExpirationDays = CouponExpirationDays.generate(),
) {
    companion object {
        fun create(
            name: String,
            quantity: Int,
            expiredDateTime: LocalDateTime = CouponExpirationDays.generate().value,
        ): Coupon =
            Coupon(
                id = CouponId.generate(),
                name = CouponName(name),
                quantity = CouponQuantity(quantity),
                expiredDateTime = CouponExpirationDays(expiredDateTime),
            )
    }

    fun issue() {
        if(expiredDateTime.isExpired()) throw CouponExpiredException()
        if(!quantity.hasRemaining()) throw CouponOutOfStockException()
        quantity = quantity.decrease()
    }
}
