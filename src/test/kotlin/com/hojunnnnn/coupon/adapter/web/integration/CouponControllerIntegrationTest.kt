package com.hojunnnnn.coupon.adapter.web.integration

import com.google.gson.Gson
import com.hojunnnnn.coupon.adapter.web.CouponCreateRequest
import com.hojunnnnn.coupon.adapter.web.HeaderKeys.USER_ID_HEADER
import com.hojunnnnn.coupon.application.port.`in`.CouponCreateCommand
import com.hojunnnnn.coupon.application.port.`in`.CouponUseCase
import com.hojunnnnn.coupon.domain.CouponStatus
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import org.springframework.transaction.annotation.Transactional

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class CouponControllerIntegrationTest
    @Autowired
    constructor(
        private val mockMvc: MockMvc,
        private val gson: Gson,
        private val couponUseCase: CouponUseCase,
    ) {
        @Nested
        inner class `쿠폰 생성` {
            private val url = "/api/v1/coupons"

            @ParameterizedTest
            @CsvSource(
                "'a  b', 10",
                "'   ', 10",
                "TEST_COUPON, -1",
                "TEST_COUPON, 0",
            )
            fun `유효하지 않은 파라미터로 요청시 에러를 반환한다`(
                name: String,
                quantity: Int,
            ) {
                val request = CouponCreateRequest(name, quantity)

                val resultActions =
                    mockMvc.post(url) {
                        contentType = MediaType.APPLICATION_JSON
                        content = gson.toJson(request)
                    }

                resultActions.andExpect { status { isBadRequest() } }
            }

            @Test
            fun `성공`() {
                val name = "TEST_COUPON"
                val quantity = 10
                val request = CouponCreateRequest(name, quantity)

                val resultActions =
                    mockMvc.post(url) {
                        contentType = MediaType.APPLICATION_JSON
                        content = gson.toJson(request)
                    }

                resultActions
                    .andExpect { status { isOk() } }
                    .andExpect { jsonPath("$.name") { value(name) } }
                    .andExpect { jsonPath("$.quantity") { value(quantity) } }
                    .andDo { print() }
            }
        }

        @Nested
        inner class `쿠폰 발급` {
            @Test
            fun `사용자 식별값이 헤더에 존재하지 않으면 예외를 반환한다`() {
                val resultActions = mockMvc.post("/api/v1/coupons/1/issue")

                resultActions.andExpect { status { isBadRequest() } }
            }

            @Test
            fun `성공`() {
                val coupon = couponUseCase.createCoupon(CouponCreateCommand.of("TEST_COUPON", 10))
                val userId = "user-1234"
                val resultActions =
                    mockMvc.post("/api/v1/coupons/${coupon.id}/issue") {
                        header(USER_ID_HEADER, userId)
                    }

                resultActions
                    .andExpect { status { isOk() } }
                    .andExpect { jsonPath("$.couponId") { value(coupon.id) } }
                    .andExpect { jsonPath("$.userId") { value(userId) } }
                    .andExpect { jsonPath("$.couponStatus") { value(CouponStatus.ISSUED.name) } }
                    .andDo { print() }
            }
        }
    }
