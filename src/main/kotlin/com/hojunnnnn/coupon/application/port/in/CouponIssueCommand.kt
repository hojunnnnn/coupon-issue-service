package com.hojunnnnn.coupon.application.port.`in`

data class CouponIssueCommand(
    val userId: String,
    val couponId: Long,
)