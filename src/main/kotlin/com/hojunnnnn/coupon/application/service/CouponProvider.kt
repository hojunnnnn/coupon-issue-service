package com.hojunnnnn.coupon.application.service

import com.hojunnnnn.coupon.adapter.web.CouponCreateResponse
import com.hojunnnnn.coupon.application.port.out.CouponRepository
import com.hojunnnnn.coupon.domain.Coupon
import com.hojunnnnn.coupon.domain.CouponName
import com.hojunnnnn.coupon.domain.CouponQuantity
import com.hojunnnnn.coupon.errors.CouponDuplicationException
import com.hojunnnnn.coupon.errors.CouponNotFoundException
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Component
class CouponProvider(
    private val couponRepository: CouponRepository,
) {
    @Transactional
    fun createCoupon(
        name: CouponName,
        quantity: CouponQuantity,
    ): CouponCreateResponse {
        if (couponRepository.existsByName(name.value)) {
            throw CouponDuplicationException()
        }
        val savedCoupon = couponRepository.save(Coupon(name = name, quantity = quantity))
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
            ?: throw CouponNotFoundException()
    }
}
