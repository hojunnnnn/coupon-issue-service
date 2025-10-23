package com.hojunnnnn.coupon.application.service

import com.hojunnnnn.coupon.application.port.`in`.CouponCreateResponse
import com.hojunnnnn.coupon.application.port.out.CouponRepository
import com.hojunnnnn.coupon.domain.Coupon
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class CouponProvider(
    private val couponRepository: CouponRepository,
) {


    @Transactional
    fun createCoupon(name: String, quantity: Int): CouponCreateResponse {
        if(couponRepository.existsByName(name)) { throw Exception() }
        val savedCoupon = couponRepository.save(Coupon(name = name, quantity = quantity))
        return CouponCreateResponse(
            id = savedCoupon.id,
            name = savedCoupon.name,
            quantity = savedCoupon.quantity,
            expiredDateTime = savedCoupon.expiredDateTime,
        )
    }

    fun findBy(couponId: Long): Coupon {
        return couponRepository.findById(couponId)
    }
}