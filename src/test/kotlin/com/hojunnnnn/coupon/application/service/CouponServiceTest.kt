package com.hojunnnnn.coupon.application.service

import com.hojunnnnn.coupon.application.port.out.CouponRepository
import com.hojunnnnn.coupon.domain.Coupon
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any

@ExtendWith(MockitoExtension::class)
class CouponServiceTest {

    @Mock
    private lateinit var couponRepository: CouponRepository

    @InjectMocks
    private lateinit var couponService: CouponService



    @Nested
    inner class `쿠폰 생성` {

        @Test
        fun `같은 이름이 존재하는 경우 예외가 발생한다`() {
            val name = "TEST_COUPON"
            given(couponRepository.existsByName(name))
                .willReturn(true)

            assertThrows<Exception> { couponService.createCoupon(name, 10) }

        }

        @Test
        fun `성공`() {
            // given
            val name = "TEST_COUPON"
            val quantity = 10
            given(couponRepository.existsByName(name))
                .willReturn(false)
            given(couponRepository.save(any()))
                .willReturn(Coupon(name = name, quantity = quantity))

            // when
            val result = couponService.createCoupon(name, quantity)

            // then
            assertThat(result).isNotNull()
            assertThat(result.name).isEqualTo(name)
            assertThat(result.quantity).isEqualTo(quantity)
        }
    }

}


