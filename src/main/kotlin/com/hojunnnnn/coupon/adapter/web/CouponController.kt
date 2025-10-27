package com.hojunnnnn.coupon.adapter.web

import com.hojunnnnn.coupon.adapter.web.HeaderKeys.USER_ID_HEADER
import com.hojunnnnn.coupon.application.port.`in`.CouponUseCase
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class CouponController(
    private val couponUseCase: CouponUseCase,
) {
    @PostMapping("/api/v1/coupons")
    fun createCoupon(
        @RequestBody @Valid request: CouponCreateRequest,
    ): ResponseEntity<CouponCreateResponse> {
        val response = couponUseCase.createCoupon(request.name, request.quantity)
        return ResponseEntity.ok(response)
    }

    @PostMapping("/api/v1/coupons/{id}/issue")
    fun issueCoupon(
        @RequestHeader(USER_ID_HEADER) userId: String,
        @PathVariable id: Long,
    ): ResponseEntity<CouponIssueResponse> {
        val response = couponUseCase.issueCoupon(userId, id)
        return ResponseEntity.ok(response)
    }

    @PostMapping("/api/v1/coupons/event/issue")
    fun issueEventCoupon(
        @RequestHeader(USER_ID_HEADER) userId: String,
    ): ResponseEntity<CouponIssueResponse> {
        val response = couponUseCase.issueEventCoupon(userId)
        return ResponseEntity.ok(response)
    }

}
