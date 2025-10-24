package com.hojunnnnn.coupon.adapter.persistence

import com.hojunnnnn.coupon.domain.Coupon
import org.springframework.data.jpa.repository.JpaRepository

interface CouponJpaRepository : JpaRepository<Coupon, Long> {
    fun existsByName(name: String): Boolean
}
