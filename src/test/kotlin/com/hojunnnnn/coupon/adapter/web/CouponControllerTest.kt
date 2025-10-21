package com.hojunnnnn.coupon.adapter.web

import com.google.gson.Gson
import com.hojunnnnn.coupon.application.port.`in`.CouponCreateResponse
import com.hojunnnnn.coupon.application.port.`in`.CouponUseCase
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@WebMvcTest(CouponController::class)
class CouponControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var couponUseCase: CouponUseCase

    private val url = "/api/v1/coupons"


    @Test
    fun `쿠폰을 생성한다`() {
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
}

data class CouponCreateRequest(
    @field:NotBlank
    val name: String,

    @field:Positive
    val quantity: Int,
)

@RestController
class CouponController(
    private val couponUseCase: CouponUseCase,
) {

    @PostMapping("/api/v1/coupons")
    fun createCoupon(@RequestBody @Valid request: CouponCreateRequest
    ): ResponseEntity<CouponCreateResponse> {
        val response = couponUseCase.createCoupon(request.name, request.quantity)
        println("response: $response" )
        return ResponseEntity.ok(response)
    }

}