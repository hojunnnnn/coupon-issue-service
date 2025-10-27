package com.hojunnnnn.coupon.application.service

import com.hojunnnnn.coupon.adapter.web.CouponIssueResponse
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReentrantLock

@Component
class CouponLockManager(
    private val couponIssuer: CouponIssuer,
    private val couponProvider: CouponProvider,
) {
    private val lock = ConcurrentHashMap<Long, ReentrantLock>()

    private fun getLock(couponId: Long): ReentrantLock =
        lock.computeIfAbsent(couponId) { ReentrantLock() }

    fun issueCoupon(
        userId: String,
        couponId: Long,
    ): CouponIssueResponse {
        val lock = getLock(couponId)

        if (!lock.tryLock(2, TimeUnit.SECONDS)) {
            throw Exception("쿠폰 발급이 지연되고 있습니다. 잠시 후 다시 시도해주세요.")
        }

        try {
            return couponIssuer.issueCoupon(userId, couponId)
        } finally {
            lock.unlock()
        }
    }

    fun issueEventCoupon(userId: String
    ): CouponIssueResponse {
        val eventCoupon = couponProvider.getTodayEventCoupon()

        val lock = getLock(eventCoupon.id.value)

        if (!lock.tryLock(2, TimeUnit.SECONDS)) {
            throw Exception("쿠폰 발급이 지연되고 있습니다. 잠시 후 다시 시도해주세요.")
        }

        try {
            return couponIssuer.issueCoupon(userId, eventCoupon.id.value)
        } finally {
            lock.unlock()
        }
    }
}
