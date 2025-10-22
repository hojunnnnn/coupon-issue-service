package com.hojunnnnn.coupon.adapter.web

import com.google.gson.Gson
import com.hojunnnnn.coupon.adapter.web.HeaderKey.USER_ID_HEADER
import com.hojunnnnn.coupon.application.port.`in`.CouponCreateRequest
import com.hojunnnnn.coupon.application.port.`in`.CouponCreateResponse
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime

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
            val expiredDateTime = LocalDateTime.now().plusDays(7)

            given(couponUseCase.createCoupon(name, quantity))
                .willReturn(CouponCreateResponse(
                    id = 1L,
                    name = name,
                    quantity = quantity,
                    expiredDateTime = expiredDateTime,
                ))

            // when
            val resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                    .content(Gson().toJson(CouponCreateRequest(name = name, quantity = quantity)))
                    .contentType(MediaType.APPLICATION_JSON)
            )

            // then
            resultActions.andExpect {
                status().isOk()
                jsonPath("$.id").value(1L)
                jsonPath("$.name").value(name)
                jsonPath("$.quantity").value(quantity)
                jsonPath("$.expiredDateTime").value(expiredDateTime)
            }

        }

        @ParameterizedTest
        @ValueSource(ints = [-1, 0, -100])
        fun `수량이 0 이하일 때 BadRequest를 반환한다`(quantity: Int) {
            // given
            val name = "TEST_COUPON"

            // when
            val resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                    .content(Gson().toJson(CouponCreateRequest(name = name, quantity = quantity)))
                    .contentType(MediaType.APPLICATION_JSON)
            )

            // then
            resultActions.andExpect(status().isBadRequest())
        }

        @ParameterizedTest
        @ValueSource(strings = ["", "  ", "a  b"])
        fun `이름에 공백을 포함하면 BadRequest를 반환한다`(name: String) {
            // given
            val quantity = 100

            // when
            val resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                    .content(Gson().toJson(CouponCreateRequest(name = name, quantity = quantity)))
                    .contentType(MediaType.APPLICATION_JSON)
            )

            // then
            resultActions.andExpect(status().isBadRequest())
        }
    }

    @Nested
    inner class `쿠폰 발행` {
        private val url = "/api/v1/coupons/1/issue"

        @Test
        fun `사용자 식별값이 헤더에 존재하지 않으면 예외를 반환한다`() {
            // when
            val resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                    .contentType(MediaType.APPLICATION_JSON)
            )

            // then
            resultActions.andExpect(status().isBadRequest())
        }

        @Test
        fun `성공`() {
            // given
            val userId = "USER1"

            // when
            val resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                    .header(USER_ID_HEADER, userId)
                    .contentType(MediaType.APPLICATION_JSON)
            )

            // then
            resultActions.andExpect(status().isOk())
        }
    }


}

object HeaderKey {
    const val USER_ID_HEADER = "X-USER-ID"
}


