package com.hojunnnnn.coupon.application.port.`in`

import com.hojunnnnn.coupon.domain.CouponId
import com.hojunnnnn.coupon.domain.UserId

data class CouponIssueCommand(
    val userId: UserId,
    val couponId: CouponId,
) {
    companion object {

        fun of(
            userId: String,
            couponId: Long
        ): CouponIssueCommand {
            return CouponIssueCommand(
                userId = UserId(userId),
                couponId = CouponId(couponId),
            )
        }

    }
}