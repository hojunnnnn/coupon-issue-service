package com.hojunnnnn.coupon.application.port.`in`

import com.hojunnnnn.coupon.application.port.out.CouponRepository
import com.hojunnnnn.coupon.application.service.CouponService
import com.hojunnnnn.coupon.domain.Coupon
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.BDDMockito
import org.mockito.Mockito.mock
import org.mockito.kotlin.any

class CouponUseCaseTest {

    private lateinit var couponRepository: CouponRepository
    private lateinit var couponUseCase: CouponUseCase

    @BeforeEach
    fun init() {
        couponRepository = mock(CouponRepository::class.java)
        couponUseCase = CouponService(couponRepository)
    }


    @Nested
    inner class `쿠폰 생성` {

        @Test
        fun `같은 이름이 존재하는 경우 예외가 발생한다`() {
            val name = "TEST_COUPON"
            BDDMockito.given(couponRepository.existsByName(name))
                .willReturn(true)

            assertThrows<Exception> { couponUseCase.createCoupon(name, 10) }

        }

        @Test
        fun `성공`() {
            // given
            val name = "TEST_COUPON"
            val quantity = 10
            BDDMockito.given(couponRepository.existsByName(name))
                .willReturn(false)
            BDDMockito.given(couponRepository.save(any()))
                .willReturn(Coupon(name = name, quantity = quantity))

            // when
            val result = couponUseCase.createCoupon(name, quantity)

            // then
            Assertions.assertThat(result).isNotNull()
            Assertions.assertThat(result.name).isEqualTo(name)
            Assertions.assertThat(result.quantity).isEqualTo(quantity)
        }
    }

}