package com.hojunnnnn.coupon.application.service

import com.hojunnnnn.coupon.adapter.web.CouponIssueResponse
import com.hojunnnnn.coupon.application.port.out.CouponRepository
import com.hojunnnnn.coupon.application.port.out.UserCouponRepository
import com.hojunnnnn.coupon.domain.CouponId
import com.hojunnnnn.coupon.domain.UserId
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
        userId: UserId,
        couponId: CouponId,
    ): CouponIssueResponse {
        val coupon = couponRepository.findById(couponId.value) ?: throw CouponNotFoundException()
        if (userCouponRepository.isAlreadyIssuedCoupon(userId.value, coupon.id.value)) {
            throw CouponAlreadyIssuedException()
        }

        coupon.issue()
        couponRepository.save(coupon)

        val issuedCoupon = userCouponRepository.issueCouponTo(userId.value, coupon)
        return CouponIssueResponse(
            userId = issuedCoupon.userId.value,
            couponId = issuedCoupon.couponId.value,
            couponStatus = issuedCoupon.status.name,
        )
    }

}
