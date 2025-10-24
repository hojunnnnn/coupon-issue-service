package com.hojunnnnn.coupon.application.service

import com.hojunnnnn.coupon.domain.UserCoupon
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReentrantLock

@Component
class CouponLockManager(
    private val couponIssuer: CouponIssuer,
    private val reentrantLock: ReentrantLock = ReentrantLock(),
) {

    fun issueCoupon(userId: String, couponId: Long): UserCoupon {

        if (!reentrantLock.tryLock(2, TimeUnit.SECONDS)) {
            throw Exception("쿠폰 발급이 지연되고 있습니다. 잠시 후 다시 시도해주세요.")
        }

        try {
            return couponIssuer.issueCoupon(userId, couponId)
        } finally {
            reentrantLock.unlock()
        }
    }

}