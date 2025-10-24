package com.hojunnnnn.coupon.domain

@JvmInline
value class UserCouponId(val value: Long) {
    companion object {
        fun generate(): UserCouponId = UserCouponId(0L) // Auto-increment will assign the actual ID
    }

    init {
        require(value >= 0) { "Coupon Id must be non-negative" }
    }
}


@JvmInline
value class UserId(val value: String) {
    init {
        require(value.isNotEmpty()) { "User Id must be non-empty" }
    }
}



enum class CouponStatus {
    ISSUED,
    USED,
    EXPIRED,
}
