package com.hojunnnnn.coupon.adapter.persistence

import com.hojunnnnn.coupon.adapter.persistence.mapper.CouponPersistenceMapper
import com.hojunnnnn.coupon.application.port.out.CouponRepository
import com.hojunnnnn.coupon.domain.Coupon
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * JPA 쿠폰 데이터 접근 구현체
 * 헥사고날 아키텍처의 아웃바운드 어댑터
 */
@Component
class CouponPersistenceAdapter(
    private val couponJpaRepository: CouponJpaRepository,
    private val couponPersistenceMapper: CouponPersistenceMapper,
) : CouponRepository {
    override fun save(coupon: Coupon): Coupon {
        val entity = couponPersistenceMapper.toEntity(coupon)
        val savedEntity = couponJpaRepository.save(entity)
        return couponPersistenceMapper.toDomain(savedEntity)
    }

    override fun existsByName(name: String): Boolean = couponJpaRepository.existsByName(name)

    override fun findById(id: Long): Coupon {
        val entity = couponJpaRepository.findByIdOrNull(id) ?: throw Exception()
        return couponPersistenceMapper.toDomain(entity)
    }

    override fun findEventCoupon(
        from: LocalDateTime,
        to: LocalDateTime,
    ): Coupon? {
        val entity = couponJpaRepository.findEventCoupon(from, to).firstOrNull() ?: return null
        return couponPersistenceMapper.toDomain(entity)
    }
}
