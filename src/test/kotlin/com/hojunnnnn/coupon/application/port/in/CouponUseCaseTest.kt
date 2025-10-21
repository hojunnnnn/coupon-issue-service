package com.hojunnnnn.coupon.application.port.`in`

import com.hojunnnnn.coupon.application.port.out.CouponRepository
import com.hojunnnnn.coupon.application.port.out.UserCouponRepository
import com.hojunnnnn.coupon.application.service.CouponService
import com.hojunnnnn.coupon.domain.Coupon
import com.hojunnnnn.coupon.domain.CouponStatus
import com.hojunnnnn.coupon.domain.UserCoupon
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.BDDMockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.kotlin.any
import org.mockito.kotlin.given
import java.time.LocalDateTime

class CouponUseCaseTest {

    private lateinit var userCouponRepository: UserCouponRepository
    private lateinit var couponRepository: CouponRepository
    private lateinit var couponUseCase: CouponUseCase

    @BeforeEach
    fun init() {
        couponRepository = mock(CouponRepository::class.java)
        userCouponRepository = mock(UserCouponRepository::class.java)
        couponUseCase = CouponService(couponRepository, userCouponRepository)
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
            given(couponRepository.existsByName(name))
                .willReturn(false)
            given(couponRepository.save(any()))
                .willReturn(Coupon(name = name, quantity = quantity))

            // when
            val result = couponUseCase.createCoupon(name, quantity)

            // then
            assertThat(result).isNotNull()
            assertThat(result.name).isEqualTo(name)
            assertThat(result.quantity).isEqualTo(quantity)
        }
    }

    @Nested
    inner class `쿠폰 발행` {

        @Test
        fun `쿠폰이 존재하지 않으면 예외가 발생한다`() {
            val userId = "USER1"
            val couponId = 1L
            given(couponRepository.findById(any()))
                .willReturn(null)

            // when
            assertThrows<Exception> { couponUseCase.issueCoupon(userId, couponId) }

            // then
            verify(userCouponRepository, never()).issueCouponTo(any(), any())
        }

        @Test
        fun `남은 수량이 0일 경우 예외가 발생한다`() {
            // given
            val userId = "USER1"
            val couponId = 1L
            given(couponRepository.findById(any()))
                .willReturn(Coupon(id = 1L, name = "TEST_COUPON", quantity = 0))

            // when
            assertThrows<Exception> { couponUseCase.issueCoupon(userId, couponId) }

            // then
            verify(userCouponRepository, never()).issueCouponTo(any(), any())
        }

        @Test
        fun `만료된 쿠폰이면 예외가 발생한다`() {
            // given
            val userId = "USER1"
            val couponId = 1L
            given(couponRepository.findById(any()))
                .willReturn(Coupon(id = 1L, name = "TEST_COUPON", quantity = 10, expiredDateTime = LocalDateTime.now().minusDays(1)))

            // when
            assertThrows<Exception> { couponUseCase.issueCoupon(userId, couponId) }

            // then
            verify(userCouponRepository, never()).issueCouponTo(any(), any())

        }

        @Test
        fun `이미 발급된 쿠폰이면 예외가 발생한다`() {
            val userId = "USER1"
            val couponId = 1L
            given(userCouponRepository.isAlreadyIssuedCoupon(any(), any()))
                .willReturn(true)

            assertThrows<Exception> { couponUseCase.issueCoupon(userId, couponId) }
        }

        @Test
        fun `성공`() {
            // given
            val userId = "USER1"
            val couponId = 1L
            given(couponRepository.findById(any()))
                .willReturn(Coupon(id = couponId, name = "TEST_COUPON", quantity = 10))
            given(userCouponRepository.issueCouponTo(any(), any()))
                .willReturn(UserCoupon(
                    userId = userId,
                    couponId = couponId,
                    status = CouponStatus.ISSUED)
                )

            // when
            val result = couponUseCase.issueCoupon(userId, couponId)

            // then
            assertThat(result).isNotNull()
            assertThat(result.userId).isEqualTo(userId)
            assertThat(result.couponId).isEqualTo(couponId)
        }
    }

}