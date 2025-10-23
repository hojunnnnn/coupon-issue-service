package com.hojunnnnn.coupon.application.service

import com.hojunnnnn.coupon.domain.UserCoupon
import org.springframework.stereotype.Component

@Component
class CouponLockManager(
    private val couponIssuer: CouponIssuer,
) {

    fun issueCoupon(userId: String, couponId: Long): UserCoupon {
        synchronized(this) {
            // Transaction commit 이후 락 해제
            return couponIssuer.issueCoupon(userId, couponId)
        }
    }

}