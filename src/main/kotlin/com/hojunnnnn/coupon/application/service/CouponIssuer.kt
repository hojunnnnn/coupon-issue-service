package com.hojunnnnn.coupon.application.service

import com.hojunnnnn.coupon.adapter.web.CouponIssueResponse
import com.hojunnnnn.coupon.application.port.out.CouponRepository
import com.hojunnnnn.coupon.application.port.out.UserCouponRepository
import com.hojunnnnn.coupon.domain.Coupon
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

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
        val coupon = couponRepository.findById(couponId)
        return issue(userId, coupon)
    }

    @Transactional
    fun issueEventCoupon(userId: String): CouponIssueResponse {
        val today = LocalDate.now()
        val from = today.atStartOfDay()
        val to = today.plusDays(1).atStartOfDay()
        val eventCoupon = couponRepository.findEventCoupon(from, to)
            ?: throw Exception("today event coupon is not exist")

        return issue(userId, eventCoupon)
    }

    private fun issue(
        userId: String,
        coupon: Coupon
    ): CouponIssueResponse {
        if (userCouponRepository.isAlreadyIssuedCoupon(userId, coupon.id.value)) {
            throw Exception()
        }

        coupon.decreaseQuantity()
        couponRepository.save(coupon)

        val issuedCoupon = userCouponRepository.issueCouponTo(userId, coupon)
        return CouponIssueResponse(
            userId = issuedCoupon.userId.value,
            couponId = issuedCoupon.couponId.value,
            couponStatus = issuedCoupon.status.name,
        )
    }
}
