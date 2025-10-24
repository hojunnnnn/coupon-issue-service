package com.hojunnnnn.coupon.adapter.persistence

import com.hojunnnnn.coupon.adapter.persistence.entity.CouponEntity
import org.springframework.data.jpa.repository.JpaRepository

interface CouponJpaRepository : JpaRepository<CouponEntity, Long> {
    fun existsByName(name: String): Boolean
}
