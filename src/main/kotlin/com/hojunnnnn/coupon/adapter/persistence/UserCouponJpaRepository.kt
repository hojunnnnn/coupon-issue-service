package com.hojunnnnn.coupon.adapter.persistence

import com.hojunnnnn.coupon.domain.UserCoupon
import org.springframework.data.jpa.repository.JpaRepository

interface UserCouponJpaRepository : JpaRepository<UserCoupon, Long> {

}