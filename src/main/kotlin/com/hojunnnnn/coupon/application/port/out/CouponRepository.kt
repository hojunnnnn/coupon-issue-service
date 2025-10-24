package com.hojunnnnn.coupon.application.port.out

import com.hojunnnnn.coupon.domain.Coupon

/**
 * 쿠폰 데이터 접근을 위한 인터페이스
 * 헥사고날 아키텍처의 아웃바운드 포트
 */
interface CouponRepository {
    fun save(coupon: Coupon): Coupon

    fun existsByName(name: String): Boolean

    fun findById(id: Long): Coupon
}
