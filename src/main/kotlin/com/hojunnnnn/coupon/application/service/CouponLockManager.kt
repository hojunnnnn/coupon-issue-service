package com.hojunnnnn.coupon.application.service

import com.hojunnnnn.coupon.adapter.web.CouponIssueResponse
import com.hojunnnnn.coupon.domain.CouponId
import com.hojunnnnn.coupon.domain.UserId
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

    fun issueCoupon(
        userId: UserId,
        couponId: CouponId,
    ): CouponIssueResponse {
        return withLock(couponId) {
            couponIssuer.issueCoupon(userId, couponId)
        }
    }

    fun issueEventCoupon(userId: UserId
    ): CouponIssueResponse {
        val eventCoupon = couponProvider.getTodayEventCoupon()

        return withLock(eventCoupon.id) {
            couponIssuer.issueCoupon(userId, eventCoupon.id)
        }
    }

    private fun <T> withLock(couponId: CouponId, block: () -> T): T {
        val lock = getLock(couponId.value)

        if (!lock.tryLock(2, TimeUnit.SECONDS)) {
            throw Exception("쿠폰 발급이 지연되고 있습니다. 잠시 후 다시 시도해주세요.")
        }
        try {
            return block()
        } finally {
            lock.unlock()
        }
    }

    private fun getLock(couponId: Long): ReentrantLock =
        lock.computeIfAbsent(couponId) { ReentrantLock() }
}
