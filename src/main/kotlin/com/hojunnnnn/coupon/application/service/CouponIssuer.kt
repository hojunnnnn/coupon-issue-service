package com.hojunnnnn.coupon.application.service

import com.hojunnnnn.coupon.application.port.out.UserCouponRepository
import com.hojunnnnn.coupon.domain.Coupon
import com.hojunnnnn.coupon.domain.UserCoupon
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Component
class CouponIssuer(
    private val couponProvider: CouponProvider,
    private val userCouponRepository: UserCouponRepository,
) {
    @Transactional
    fun issueCoupon(
        userId: String,
        couponId: Long,
    ): UserCoupon {
        val coupon = couponProvider.findBy(couponId)
        validateCoupon(coupon, userId)

        coupon.decreaseQuantity()

        return userCouponRepository.issueCouponTo(userId, coupon)
    }

    private fun validateCoupon(
        coupon: Coupon,
        userId: String,
    ) {
        if (coupon.isSoldOut()) {
            throw Exception()
        }
        if (coupon.isExpired(LocalDateTime.now())) {
            throw Exception()
        }
        if (userCouponRepository.isAlreadyIssuedCoupon(userId, coupon.id)) {
            throw Exception()
        }
    }
}
