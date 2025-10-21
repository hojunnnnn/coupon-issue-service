package com.hojunnnnn.coupon.application.port.`in`


/**
 * 쿠폰 유스케이스 인터페이스
 * 헥사고날 아키텍처의 인바운드 포트
 */
interface CouponUseCase {
    fun createCoupon(name: String, quantity: Int): CouponCreateResponse
}