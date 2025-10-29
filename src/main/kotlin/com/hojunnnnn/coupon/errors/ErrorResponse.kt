package com.hojunnnnn.coupon.errors

import org.springframework.http.ResponseEntity

data class ErrorResponse(
    val errorCode: ErrorCode,
    val message: String,
) {

    companion object {
        fun toResponseEntity(
            errorCode: ErrorCode,
            message: String = errorCode.message,
        ): ResponseEntity<ErrorResponse> =
            ResponseEntity
                .status(errorCode.httpStatus)
                .body(ErrorResponse(errorCode, message))
    }

}