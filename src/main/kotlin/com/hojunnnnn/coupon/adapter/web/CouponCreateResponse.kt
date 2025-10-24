package com.hojunnnnn.coupon.adapter.web

import java.time.LocalDateTime

data class CouponCreateResponse(
    val id: Long,
    val name: String,
    val quantity: Int,
    val expiredDateTime: LocalDateTime,
)
