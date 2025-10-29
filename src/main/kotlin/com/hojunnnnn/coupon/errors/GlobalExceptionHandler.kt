package com.hojunnnnn.coupon.errors

import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@RestControllerAdvice
class GlobalExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(CouponException::class)
    fun handleCouponException(
        e: CouponException,
        request: HttpServletRequest,
    ): ResponseEntity<ErrorResponse> {
        logger.error("""
            [Coupon Exception] 
            - status code : ${e.errorCode.httpStatus}
            - message : ${e.errorCode.message}
        """.trimIndent()
        )
        return ErrorResponse.toResponseEntity(e.errorCode)
    }

}

