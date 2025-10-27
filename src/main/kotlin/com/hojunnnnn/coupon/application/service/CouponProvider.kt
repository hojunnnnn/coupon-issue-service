package com.hojunnnnn.coupon.application.service

import com.hojunnnnn.coupon.adapter.web.CouponCreateResponse
import com.hojunnnnn.coupon.application.port.out.CouponRepository
import com.hojunnnnn.coupon.domain.Coupon
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Component
class CouponProvider(
    private val couponRepository: CouponRepository,
) {
    @Transactional
    fun createCoupon(
        name: String,
        quantity: Int,
    ): CouponCreateResponse {
        if (couponRepository.existsByName(name)) {
            throw Exception()
        }
        val savedCoupon = couponRepository.save(Coupon.create(name, quantity))
        return CouponCreateResponse(
            id = savedCoupon.id.value,
            name = savedCoupon.name.value,
            quantity = savedCoupon.quantity.value,
            expiredDateTime = savedCoupon.expiredDateTime.value,
        )
    }

    fun getTodayEventCoupon(): Coupon {
        val today = LocalDate.now()
        val from = today.atStartOfDay()
        val to = today.plusDays(1).atStartOfDay()
        return couponRepository.findEventCoupon(from, to)
            ?: throw Exception("today event coupon is not exist")
    }
}
