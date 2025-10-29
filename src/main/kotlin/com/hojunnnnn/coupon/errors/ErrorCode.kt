package com.hojunnnnn.coupon.errors

import org.springframework.http.HttpStatus

sealed interface ErrorCode {
    val httpStatus: HttpStatus
    val message: String


    enum class Coupon(
        override val httpStatus: HttpStatus,
        override val message: String
    ): ErrorCode {
        NOT_FOUND(HttpStatus.NOT_FOUND, "해당 쿠폰을 찾을 수 없습니다."),
        DUPLICATION(HttpStatus.BAD_REQUEST, "이미 존재하는 쿠폰입니다."),
        ALREADY_ISSUED(HttpStatus.BAD_REQUEST, "이미 발행된 쿠폰입니다."),
        EXPIRED(HttpStatus.BAD_REQUEST, "만료된 쿠폰입니다."),
        OUT_OF_STOCK(HttpStatus.BAD_REQUEST, "쿠폰이 모두 소진되었습니다.")
    }

}
