package com.hojunnnnn.coupon.application.service

import com.hojunnnnn.coupon.adapter.web.CouponIssueResponse
import com.hojunnnnn.coupon.application.port.out.CouponRepository
import com.hojunnnnn.coupon.application.port.out.UserCouponRepository
import com.hojunnnnn.coupon.errors.CouponAlreadyIssuedException
import com.hojunnnnn.coupon.errors.CouponNotFoundException
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class CouponIssuer(
    private val couponRepository: CouponRepository,
    private val userCouponRepository: UserCouponRepository,
) {
    @Transactional
    fun issueCoupon(
        userId: String,
        couponId: Long,
    ): CouponIssueResponse {
        val coupon = couponRepository.findById(couponId) ?: throw CouponNotFoundException()
        if (userCouponRepository.isAlreadyIssuedCoupon(userId, coupon.id.value)) {
            throw CouponAlreadyIssuedException()
        }

        coupon.issue()
        couponRepository.save(coupon)

        val issuedCoupon = userCouponRepository.issueCouponTo(userId, coupon)
        return CouponIssueResponse(
            userId = issuedCoupon.userId.value,
            couponId = issuedCoupon.couponId.value,
            couponStatus = issuedCoupon.status.name,
        )
    }

}
