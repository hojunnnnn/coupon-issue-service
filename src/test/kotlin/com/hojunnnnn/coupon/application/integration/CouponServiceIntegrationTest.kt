package com.hojunnnnn.coupon.application.integration

import com.hojunnnnn.coupon.application.port.`in`.CouponUseCase
import com.hojunnnnn.coupon.application.port.out.CouponRepository
import com.hojunnnnn.coupon.application.port.out.UserCouponRepository
import com.hojunnnnn.coupon.domain.Coupon
import com.hojunnnnn.coupon.domain.CouponStatus
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Transactional
@SpringBootTest
class CouponServiceIntegrationTest
    @Autowired
    constructor(
        private val couponUseCase: CouponUseCase,
        private val couponRepository: CouponRepository,
        private val userCouponRepository: UserCouponRepository,
    ) {
        @Nested
        inner class `쿠폰 생성` {
            @Test
            fun `같은 이름의 쿠폰이 존재하면 예외가 발생한다`() {
                val name = "TEST_COUPON"
                val quantity = 10
                val coupon = Coupon.create(name, quantity)
                couponRepository.save(coupon)

                assertThrows<Exception> { couponUseCase.createCoupon(name, quantity) }
            }

            @Test
            fun `성공`() {
                val name = "TEST_COUPON"
                val quantity = 10

                val result =
                    couponUseCase.createCoupon(name, quantity)

                assertThat(result).isNotNull()
                assertThat(result.name).isEqualTo(name)
                assertThat(result.quantity).isEqualTo(quantity)
            }
        }

        @Nested
        inner class `쿠폰 발급` {
            @Test
            fun `쿠폰이 존재하지 않으면 예외가 발생한다`() {
                val userId = "USER1"
                val couponId = 1L

                assertThrows<Exception> { couponUseCase.issueCoupon(userId, couponId) }
            }

            @Disabled("도메인에서 자체 검증")
            @Test
            fun `남은 수량이 0이하면 예외가 발생한다`() {
                val userId = "USER1"
                val name = "SOLD_OUT_COUPON"
                val quantity = 0
                val savedCoupon =
                    couponRepository.save(Coupon.create(name, quantity))

                assertThrows<Exception> { couponUseCase.issueCoupon(userId, savedCoupon.id.value) }
            }

            @Disabled("도메인에서 자체 검증")
            @Test
            fun `만료된 쿠폰이면 예외가 발생한다`() {
                val userId = "USER1"
                val name = "TEST_COUPON"
                val quantity = 10
                val savedCoupon =
                    couponRepository.save(Coupon.create(name, quantity, LocalDateTime.now().minusDays(1)))

                assertThrows<Exception> { couponUseCase.issueCoupon(userId, savedCoupon.id.value) }
            }

            @Test
            fun `이미 발급된 쿠폰이면 예외가 발생한다`() {
                val userId = "USER1"
                val name = "TEST_COUPON"
                val quantity = 10
                val savedCoupon = couponRepository.save(Coupon.create(name, quantity))
                userCouponRepository.issueCouponTo(userId, savedCoupon)

                assertThrows<Exception> { couponUseCase.issueCoupon(userId, savedCoupon.id.value) }
            }

            @Test
            fun `성공`() {
                val userId = "USER1"
                val name = "TEST_COUPON"
                val quantity = 10
                val savedCoupon = couponRepository.save(Coupon.create(name, quantity))

                val result = couponUseCase.issueCoupon(userId, savedCoupon.id.value)

                assertThat(result).isNotNull()
                assertThat(result.couponId).isEqualTo(savedCoupon.id.value)
                assertThat(result.userId).isEqualTo(userId)
                assertThat(result.couponStatus).isEqualTo(CouponStatus.ISSUED.name)

                // 쿠폰 수량 감소 확인
                val coupon = couponRepository.findById(result.couponId)
                assertThat(coupon.quantity.value).isEqualTo(quantity - 1)
            }
        }
    }
