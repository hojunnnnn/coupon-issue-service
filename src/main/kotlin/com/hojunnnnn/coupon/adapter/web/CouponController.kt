package com.hojunnnnn.coupon.adapter.web

import com.hojunnnnn.coupon.adapter.web.HeaderKeys.USER_ID_HEADER
import com.hojunnnnn.coupon.application.port.`in`.CouponCreateCommand
import com.hojunnnnn.coupon.application.port.`in`.CouponIssueCommand
import com.hojunnnnn.coupon.application.port.`in`.CouponUseCase
import com.hojunnnnn.coupon.application.port.`in`.EventCouponIssueCommand
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
        val command = CouponCreateCommand(
            name = request.name,
            quantity = request.quantity
        )
        val response = couponUseCase.createCoupon(command)
        return ResponseEntity.ok(response)
    }

    @PostMapping("/api/v1/coupons/{id}/issue")
    fun issueCoupon(
        @RequestHeader(USER_ID_HEADER) userId: String,
        @PathVariable id: Long,
    ): ResponseEntity<CouponIssueResponse> {
        val command = CouponIssueCommand(
            userId = userId,
            couponId = id
        )
        val response = couponUseCase.issueCoupon(command)
        return ResponseEntity.ok(response)
    }

    @PostMapping("/api/v1/coupons/event/issue")
    fun issueEventCoupon(
        @RequestHeader(USER_ID_HEADER) userId: String,
    ): ResponseEntity<CouponIssueResponse> {
        val command = EventCouponIssueCommand(
            userId = userId
        )
        val response = couponUseCase.issueEventCoupon(command)
        return ResponseEntity.ok(response)
    }

}
