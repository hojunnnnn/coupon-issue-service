package com.hojunnnnn.coupon.adapter.persistence

import com.hojunnnnn.coupon.application.port.out.CouponRepository
import com.hojunnnnn.coupon.domain.Coupon
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

/**
 * JPA 쿠폰 데이터 접근 구현체
 * 헥사고날 아키텍처의 아웃바운드 어댑터
 */
@Component
class CouponPersistenceAdapter(
    private val couponJpaRepository: CouponJpaRepository,
) : CouponRepository {
    override fun save(coupon: Coupon): Coupon = couponJpaRepository.save(coupon)

    override fun existsByName(name: String): Boolean = couponJpaRepository.existsByName(name)

    override fun findById(id: Long): Coupon = couponJpaRepository.findByIdOrNull(id) ?: throw Exception()
}
