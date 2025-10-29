package com.hojunnnnn.coupon.errors

open class CouponException(
    val errorCode: ErrorCode,
    override val message: String = errorCode.message,
): RuntimeException()

class CouponDuplicationException(): CouponException(
    ErrorCode.Coupon.DUPLICATION
)

class CouponNotFoundException(): CouponException(
    ErrorCode.Coupon.NOT_FOUND
)

class CouponExpiredException(): CouponException(
    ErrorCode.Coupon.EXPIRED
)

class CouponAlreadyIssuedException(): CouponException(
    ErrorCode.Coupon.ALREADY_ISSUED
)

class CouponOutOfStockException(): CouponException(
    ErrorCode.Coupon.OUT_OF_STOCK
)
