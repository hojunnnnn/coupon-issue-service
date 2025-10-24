package com.hojunnnnn.coupon.adapter.web

import com.google.gson.Gson
import com.hojunnnnn.coupon.adapter.web.HeaderKeys.USER_ID_HEADER
import com.hojunnnnn.coupon.application.port.`in`.CouponUseCase
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@WebMvcTest(CouponController::class)
class CouponControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var couponUseCase: CouponUseCase

    @Nested
    inner class `쿠폰 생성` {
        private val url = "/api/v1/coupons"

        @Test
        fun `성공`() {
            // given
            val name = "TEST_COUPON"
            val quantity = 100
            val expiredDateTime = LocalDateTime.now().plusDays(7).truncatedTo(ChronoUnit.MILLIS)

            given(couponUseCase.createCoupon(name, quantity))
                .willReturn(
                    CouponCreateResponse(
                        id = 1L,
                        name = name,
                        quantity = quantity,
                        expiredDateTime = expiredDateTime,
                    ),
                )

            // when
            val resultActions =
                mockMvc.post(url) {
                    contentType = MediaType.APPLICATION_JSON
                    content = Gson().toJson(CouponCreateRequest(name = name, quantity = quantity))
                }

            resultActions
                .andExpect { status { isOk() } }
                .andExpect { jsonPath("$.id") { value(1L) } }
                .andExpect { jsonPath("$.name") { value(name) } }
                .andExpect { jsonPath("$.quantity") { value(quantity) } }
                .andExpect { jsonPath("$.expiredDateTime") { value(expiredDateTime.toString()) } }
        }

        @ParameterizedTest
        @ValueSource(ints = [-1, 0, -100])
        fun `수량이 0 이하일 때 BadRequest를 반환한다`(quantity: Int) {
            // given
            val name = "TEST_COUPON"

            // when
            val resultActions =
                mockMvc.post(url) {
                    contentType = MediaType.APPLICATION_JSON
                    content = Gson().toJson(CouponCreateRequest(name = name, quantity = quantity))
                }

            // then
            resultActions.andExpect { status { isBadRequest() } }
        }

        @ParameterizedTest
        @ValueSource(strings = ["", "  ", "a  b"])
        fun `이름에 공백을 포함하면 BadRequest를 반환한다`(name: String) {
            // given
            val quantity = 100

            // when
            val resultActions =
                mockMvc.post(url) {
                    contentType = MediaType.APPLICATION_JSON
                    content = Gson().toJson(CouponCreateRequest(name = name, quantity = quantity))
                }

            // then
            resultActions.andExpect { status { isBadRequest() } }
        }
    }

    @Nested
    inner class `쿠폰 발행` {
        private val url = "/api/v1/coupons/1/issue"

        @Test
        fun `사용자 식별값이 헤더에 존재하지 않으면 예외를 반환한다`() {
            val resultActions =
                mockMvc.post(url) {
                    contentType = MediaType.APPLICATION_JSON
                }

            resultActions.andExpect { status { isBadRequest() } }
        }

        @Test
        fun `성공`() {
            val userId = "USER1"

            val resultActions =
                mockMvc.post(url) {
                    header(USER_ID_HEADER, userId)
                    contentType = MediaType.APPLICATION_JSON
                }

            resultActions.andExpect { status { isOk() } }
        }
    }
}
