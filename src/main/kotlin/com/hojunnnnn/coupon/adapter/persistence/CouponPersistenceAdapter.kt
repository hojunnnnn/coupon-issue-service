package com.hojunnnnn.coupon.adapter.persistence

import com.hojunnnnn.coupon.domain.Coupon
import com.hojunnnnn.coupon.application.port.out.CouponRepository
import org.springframework.stereotype.Component

/**
 * JPA 쿠폰 데이터 접근 구현체
 * 헥사고날 아키텍처의 아웃바운드 어댑터
 */
@Component
class CouponPersistenceAdapter(
    private val couponJpaRepository: CouponJpaRepository,
) : CouponRepository {

    override fun save(coupon: Coupon): Coupon {
        return couponJpaRepository.save(coupon)
    }

    override fun existsByName(name: String): Boolean {
        return couponJpaRepository.existsByName(name)
    }

}