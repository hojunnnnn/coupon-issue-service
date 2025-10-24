package com.hojunnnnn.coupon.domain

import java.time.LocalDateTime

class UserCoupon(
    val id: UserCouponId,
    val couponId: CouponId,
    val userId: UserId,
    val status: CouponStatus,
    val issuedDateTime: LocalDateTime = LocalDateTime.now(),
) {

    companion object {
        fun create(
            couponId: Long,
            userId: String,
            status: CouponStatus = CouponStatus.ISSUED,
        ): UserCoupon {
            return UserCoupon(
                id = UserCouponId.generate(),
                couponId = CouponId(couponId),
                userId = UserId(userId),
                status = status,
                issuedDateTime = LocalDateTime.now(),
            )
        }
    }
}
