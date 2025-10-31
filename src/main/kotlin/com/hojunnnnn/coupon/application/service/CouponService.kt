package com.hojunnnnn.coupon.application.service

import com.hojunnnnn.coupon.adapter.web.CouponCreateResponse
import com.hojunnnnn.coupon.adapter.web.CouponIssueResponse
import com.hojunnnnn.coupon.application.port.`in`.CouponCreateCommand
import com.hojunnnnn.coupon.application.port.`in`.CouponIssueCommand
import com.hojunnnnn.coupon.application.port.`in`.CouponUseCase
import com.hojunnnnn.coupon.application.port.`in`.EventCouponIssueCommand
import com.hojunnnnn.coupon.domain.CouponId
import com.hojunnnnn.coupon.domain.CouponName
import com.hojunnnnn.coupon.domain.CouponQuantity
import com.hojunnnnn.coupon.domain.UserId
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
    override fun createCoupon(couponCreateCommand: CouponCreateCommand
    ): CouponCreateResponse = couponProvider.createCoupon(
        CouponName(couponCreateCommand.name),
        CouponQuantity(couponCreateCommand.quantity)
    )

    override fun issueCoupon(couponIssueCommand: CouponIssueCommand
    ): CouponIssueResponse = couponLockManager.issueCoupon(
        UserId(couponIssueCommand.userId),
        CouponId(couponIssueCommand.couponId)
    )

    override fun issueEventCoupon(eventCouponIssueCommand: EventCouponIssueCommand
    ): CouponIssueResponse = couponLockManager.issueEventCoupon(
        UserId(eventCouponIssueCommand.userId)
    )
}
