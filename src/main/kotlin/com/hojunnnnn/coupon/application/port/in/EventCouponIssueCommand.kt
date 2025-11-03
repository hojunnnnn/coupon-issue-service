package com.hojunnnnn.coupon.application.port.`in`

import com.hojunnnnn.coupon.domain.UserId

data class EventCouponIssueCommand(
    val userId: UserId,
) {
    companion object {
        fun of(userId: String): EventCouponIssueCommand {
            return EventCouponIssueCommand(UserId(userId))
        }
    }

}