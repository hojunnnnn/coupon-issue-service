package com.hojunnnnn.coupon.application.port.`in`

import com.hojunnnnn.coupon.application.port.out.CouponRepository
import com.hojunnnnn.coupon.application.port.out.UserCouponRepository
import com.hojunnnnn.coupon.application.service.CouponIssuer
import com.hojunnnnn.coupon.application.service.CouponLockManager
import com.hojunnnnn.coupon.application.service.CouponProvider
import com.hojunnnnn.coupon.application.service.CouponService
import com.hojunnnnn.coupon.domain.Coupon
import com.hojunnnnn.coupon.domain.UserCoupon
import com.hojunnnnn.coupon.errors.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*
import org.mockito.kotlin.any
import org.mockito.kotlin.given
import java.time.LocalDateTime

class CouponUseCaseTest {
    private lateinit var couponIssuer: CouponIssuer
    private lateinit var couponLockManager: CouponLockManager
    private lateinit var couponProvider: CouponProvider
    private lateinit var couponRepository: CouponRepository
    private lateinit var userCouponRepository: UserCouponRepository
    private lateinit var couponUseCase: CouponUseCase

    @BeforeEach
    fun init() {
        couponRepository = mock(CouponRepository::class.java)
        userCouponRepository = mock(UserCouponRepository::class.java)
        couponProvider = CouponProvider(couponRepository)
        couponIssuer = CouponIssuer(couponRepository, userCouponRepository)
        couponLockManager = CouponLockManager(couponIssuer, couponProvider)
        couponUseCase = CouponService(couponLockManager, couponProvider)
    }

    @Nested
    inner class `쿠폰 생성` {
        @Test
        fun `같은 이름이 존재하는 경우 예외가 발생한다`() {
            val name = "TEST_COUPON"
            given(couponRepository.existsByName(name))
                .willReturn(true)

            assertThrows<CouponDuplicationException> { couponUseCase.createCoupon(name, 10) }
        }

        @Test
        fun `성공`() {
            // given
            val name = "TEST_COUPON"
            val quantity = 10
            given(couponRepository.existsByName(name))
                .willReturn(false)
            given(couponRepository.save(any()))
                .willReturn(Coupon.create(name, quantity))

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
            assertThrows<CouponNotFoundException> { couponUseCase.issueCoupon(userId, couponId) }

            // then
            verify(userCouponRepository, never()).issueCouponTo(any(), any())
        }

        @Test
        fun `남은 수량이 없으면 예외가 발생한다`() {
            // given
            val userId = "USER1"
            val couponId = 1L
            given(couponRepository.findById(any()))
                .willReturn(Coupon.create("TEST_COUPON", 0))

            // when
            assertThrows<CouponOutOfStockException> { couponUseCase.issueCoupon(userId, couponId) }

            // then
            verify(userCouponRepository, never()).issueCouponTo(any(), any())
        }

        @Test
        fun `만료된 쿠폰이면 예외가 발생한다`() {
            // given
            val userId = "USER1"
            val couponId = 1L
            given(couponRepository.findById(any()))
                .willReturn(Coupon.create("TEST_COUPON", 10, expiredDateTime = LocalDateTime.now().minusDays(1)))

            // when
            assertThrows<CouponExpiredException> { couponUseCase.issueCoupon(userId, couponId) }

            // then
            verify(userCouponRepository, never()).issueCouponTo(any(), any())
        }

        @Test
        fun `이미 발급된 쿠폰이면 예외가 발생한다`() {
            val userId = "USER1"
            val couponId = 1L
            given(couponRepository.findById(any()))
                .willReturn(Coupon.create("TEST_COUPON", 10))
            given(userCouponRepository.isAlreadyIssuedCoupon(any(), any()))
                .willReturn(true)

            assertThrows<CouponAlreadyIssuedException> { couponUseCase.issueCoupon(userId, couponId) }
        }

        @Test
        fun `성공`() {
            // given
            val userId = "USER1"
            val couponId = 1L
            given(couponRepository.findById(any()))
                .willReturn(Coupon.create("TEST_COUPON", 10))
            given(userCouponRepository.isAlreadyIssuedCoupon(any(), any()))
                .willReturn(false)
            given(userCouponRepository.issueCouponTo(any(), any()))
                .willReturn(UserCoupon.create(couponId, userId))

            // when
            val result = couponUseCase.issueCoupon(userId, couponId)

            // then
            assertThat(result).isNotNull()
            assertThat(result.userId).isEqualTo(userId)
            assertThat(result.couponId).isEqualTo(couponId)
        }
    }

    @Nested
    inner class `이벤트 쿠폰 발행` {

        @Test
        fun `쿠폰이 존재하지 않으면 예외가 발생한다`() {
            val userId = "USER1"
            given(couponRepository.findEventCoupon(any(), any()))
                .willReturn(null)

            assertThrows<CouponNotFoundException> { couponUseCase.issueEventCoupon(userId) }
        }

        @Test
        fun `이미 발급된 쿠폰이면 예외가 발생한다`() {
            val userId = "USER1"
            val coupon = Coupon.create("EVENT_COUPON", 10)
            given(couponRepository.findEventCoupon(any(), any()))
                .willReturn(coupon)
            given(couponRepository.findById(any()))
                .willReturn(coupon)
            given(userCouponRepository.isAlreadyIssuedCoupon(any(), any()))
                .willReturn(true)

            assertThrows<CouponAlreadyIssuedException> { couponUseCase.issueEventCoupon(userId) }
        }

        @Test
        fun `성공`() {
            val userId = "USER1"
            val couponId = 1L
            val coupon = Coupon.create("EVENT_COUPON", 10)
            given(couponRepository.findEventCoupon(any(), any()))
                .willReturn(coupon)
            given(couponRepository.findById(any()))
                .willReturn(coupon)
            given(userCouponRepository.isAlreadyIssuedCoupon(any(), any()))
                .willReturn(false)
            given(userCouponRepository.issueCouponTo(any(), any()))
                .willReturn(UserCoupon.create(couponId, userId))

            val result = couponUseCase.issueEventCoupon(userId)

            assertThat(result).isNotNull()
            assertThat(result.userId).isEqualTo(userId)
            assertThat(result.couponId).isEqualTo(couponId)
        }
    }
}
