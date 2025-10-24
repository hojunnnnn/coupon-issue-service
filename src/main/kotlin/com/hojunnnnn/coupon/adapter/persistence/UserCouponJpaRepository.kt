package com.hojunnnnn.coupon.adapter.persistence

import com.hojunnnnn.coupon.adapter.persistence.entity.UserCouponEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserCouponJpaRepository : JpaRepository<UserCouponEntity, Long> {
    fun existsByUserIdAndCouponId(
        userId: String,
        couponId: Long,
    ): Boolean
}
