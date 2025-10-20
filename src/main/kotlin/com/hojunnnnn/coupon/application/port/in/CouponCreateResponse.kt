package com.hojunnnnn.coupon.application.port.`in`

import java.time.LocalDateTime

data class CouponCreateResponse(
    val id: Long,
    val name: String,
    val quantity: Int,
    val expiredDateTime: LocalDateTime,
)