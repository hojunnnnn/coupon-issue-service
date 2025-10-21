package com.hojunnnnn.coupon.adapter.persistence

import com.hojunnnnn.coupon.application.port.out.UserCouponRepository
import com.hojunnnnn.coupon.domain.Coupon
import com.hojunnnnn.coupon.domain.CouponStatus
import com.hojunnnnn.coupon.domain.UserCoupon
import org.springframework.stereotype.Component

@Component
class UserCouponPersistenceAdapter(
    private val userCouponJpaRepository: UserCouponJpaRepository,
) : UserCouponRepository {

    override fun issueCouponTo(
        userId: String,
        coupon: Coupon
    ): UserCoupon {
        val userCoupon = UserCoupon(
            couponId = coupon.id,
            userId = userId,
            status = CouponStatus.ISSUED,
        )
        return userCouponJpaRepository.save(userCoupon)
    }

}