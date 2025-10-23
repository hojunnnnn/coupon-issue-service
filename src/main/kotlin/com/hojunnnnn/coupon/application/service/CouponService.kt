package com.hojunnnnn.coupon.application.service

import com.hojunnnnn.coupon.application.port.`in`.CouponCreateResponse
import com.hojunnnnn.coupon.application.port.`in`.CouponUseCase
import com.hojunnnnn.coupon.domain.UserCoupon
import org.springframework.stereotype.Service

/**
 * 쿠폰 관련 비즈니스 로직 처리 서비스
 * 헥사고날 아키텍처의 애플리케이션 서비스
 */
@Service
class CouponService(
    private val couponLockManager: CouponLockManager,
    private val couponProvider: CouponProvider,
) : CouponUseCase {


    override fun createCoupon(name: String, quantity: Int): CouponCreateResponse {
        return couponProvider.createCoupon(name, quantity)
    }

    override fun issueCoupon(userId: String, couponId: Long): UserCoupon {
        return couponLockManager.issueCoupon(userId, couponId)
    }
}