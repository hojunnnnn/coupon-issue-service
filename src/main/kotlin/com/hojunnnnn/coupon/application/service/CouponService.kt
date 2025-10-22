package com.hojunnnnn.coupon.application.service

import com.hojunnnnn.coupon.application.port.`in`.CouponCreateResponse
import com.hojunnnnn.coupon.application.port.`in`.CouponUseCase
import com.hojunnnnn.coupon.application.port.out.CouponRepository
import com.hojunnnnn.coupon.application.port.out.UserCouponRepository
import com.hojunnnnn.coupon.domain.Coupon
import com.hojunnnnn.coupon.domain.UserCoupon
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

/**
 * 쿠폰 관련 비즈니스 로직 처리 서비스
 * 헥사고날 아키텍처의 애플리케이션 서비스
 */
@Service
class CouponService(
    private val couponRepository: CouponRepository,
    private val userCouponRepository: UserCouponRepository,
) : CouponUseCase {

    @Transactional
    override fun createCoupon(name: String, quantity: Int): CouponCreateResponse {
        if(couponRepository.existsByName(name)) { throw Exception() }
        val savedCoupon = couponRepository.save(Coupon(name = name, quantity = quantity))
        return CouponCreateResponse(
            id = savedCoupon.id,
            name = savedCoupon.name,
            quantity = savedCoupon.quantity,
            expiredDateTime = savedCoupon.expiredDateTime,
        )
    }

    @Transactional
    override fun issueCoupon(userId: String, couponId: Long): UserCoupon {
        val coupon = couponRepository.findById(couponId)
        validateCoupon(coupon, userId)

        coupon.decreaseQuantity()

        return userCouponRepository.issueCouponTo(userId, coupon)
    }

    private fun validateCoupon(coupon: Coupon, userId: String) {
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