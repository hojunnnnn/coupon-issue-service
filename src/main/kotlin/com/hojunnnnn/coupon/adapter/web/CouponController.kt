package com.hojunnnnn.coupon.adapter.web

import com.hojunnnnn.coupon.application.port.`in`.CouponCreateRequest
import com.hojunnnnn.coupon.application.port.`in`.CouponCreateResponse
import com.hojunnnnn.coupon.application.port.`in`.CouponUseCase
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
class CouponController(
    private val couponUseCase: CouponUseCase,
) {

    @PostMapping("/api/v1/coupons")
    fun createCoupon(@RequestBody @Valid request: CouponCreateRequest
    ): ResponseEntity<CouponCreateResponse> {
        val response = couponUseCase.createCoupon(request.name, request.quantity)
        return ResponseEntity.ok(response)
    }

    @PostMapping("/api/v1/coupons/{id}/issue")
    fun issueCoupon(
        @RequestHeader("X-USER-ID") userId: String,
        @PathVariable id: Long,
    ): ResponseEntity<Unit> {
        couponUseCase.issueCoupon(userId, id)
        return ResponseEntity.ok().build()
    }

}