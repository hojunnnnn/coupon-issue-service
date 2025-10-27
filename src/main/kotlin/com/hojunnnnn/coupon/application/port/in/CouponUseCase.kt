package com.hojunnnnn.coupon.application.port.`in`

import com.hojunnnnn.coupon.adapter.web.CouponCreateResponse
import com.hojunnnnn.coupon.adapter.web.CouponIssueResponse

/**
 * 쿠폰 유스케이스 인터페이스
 * 헥사고날 아키텍처의 인바운드 포트
 */
interface CouponUseCase {
    fun createCoupon(
        name: String,
        quantity: Int,
    ): CouponCreateResponse

    fun issueCoupon(
        userId: String,
        couponId: Long,
    ): CouponIssueResponse

    fun issueEventCoupon(
        userId: String,
    ): CouponIssueResponse
}
