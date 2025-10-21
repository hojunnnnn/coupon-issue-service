package com.hojunnnnn.coupon.application.port.out

import com.hojunnnnn.coupon.domain.Coupon
import com.hojunnnnn.coupon.domain.UserCoupon

interface UserCouponRepository {

    fun issueCouponTo(userId: String, coupon: Coupon): UserCoupon
}