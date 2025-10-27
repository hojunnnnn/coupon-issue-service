package com.hojunnnnn.coupon.adapter.persistence

import com.hojunnnnn.coupon.adapter.persistence.entity.CouponEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDateTime

interface CouponJpaRepository : JpaRepository<CouponEntity, Long> {
    fun existsByName(name: String): Boolean

    @Query("""
        select c from CouponEntity c
        where c.createdDateTime between :from and :to
        order by c.id desc
    """)
    fun findEventCoupon(
        from: LocalDateTime,
        to: LocalDateTime
    ): List<CouponEntity>
}
