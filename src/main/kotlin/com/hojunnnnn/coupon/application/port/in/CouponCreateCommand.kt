package com.hojunnnnn.coupon.application.port.`in`

import com.hojunnnnn.coupon.domain.CouponName
import com.hojunnnnn.coupon.domain.CouponQuantity

data class CouponCreateCommand(
    val name: CouponName,
    val quantity: CouponQuantity,
) {
    companion object {
        fun of(
            name: String,
            quantity: Int
        ): CouponCreateCommand {
            return CouponCreateCommand(
                name = CouponName(name),
                quantity = CouponQuantity(quantity),
            )
        }

    }
}