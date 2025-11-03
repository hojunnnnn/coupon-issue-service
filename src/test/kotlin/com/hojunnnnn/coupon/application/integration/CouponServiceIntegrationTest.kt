package com.hojunnnnn.coupon.application.integration

import com.hojunnnnn.coupon.application.port.`in`.CouponCreateCommand
import com.hojunnnnn.coupon.application.port.`in`.CouponIssueCommand
import com.hojunnnnn.coupon.application.port.`in`.CouponUseCase
import com.hojunnnnn.coupon.application.port.out.CouponRepository
import com.hojunnnnn.coupon.application.port.out.UserCouponRepository
import com.hojunnnnn.coupon.domain.Coupon
import com.hojunnnnn.coupon.domain.CouponStatus
import com.hojunnnnn.coupon.errors.CouponAlreadyIssuedException
import com.hojunnnnn.coupon.errors.CouponDuplicationException
import com.hojunnnnn.coupon.errors.CouponExpiredException
import com.hojunnnnn.coupon.errors.CouponNotFoundException
import com.hojunnnnn.coupon.errors.CouponOutOfStockException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDateTime

@SpringBootTest
class CouponServiceIntegrationTest
    @Autowired
    constructor(
        private val couponUseCase: CouponUseCase,
        private val couponRepository: CouponRepository,
        private val userCouponRepository: UserCouponRepository,
    ) {

    @AfterEach
    fun clean() {
        userCouponRepository.deleteAll()
        couponRepository.deleteAll()
    }

    @Nested
        inner class `쿠폰 생성` {
            @Test
            fun `같은 이름의 쿠폰이 존재하면 예외가 발생한다`() {
                val name = "TEST_COUPON"
                val quantity = 10
                val command = CouponCreateCommand.of(name, quantity)
                val coupon = Coupon.create(name, quantity)
                couponRepository.save(coupon)

                assertThrows<CouponDuplicationException> { couponUseCase.createCoupon(command) }
            }

            @Test
            fun `성공`() {
                val name = "TEST_COUPON"
                val quantity = 10
                val command = CouponCreateCommand.of(name, quantity)

                val result =
                    couponUseCase.createCoupon(command)

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
                val couponId = 999L
                val command = CouponIssueCommand.of(userId, couponId)

                assertThrows<CouponNotFoundException> { couponUseCase.issueCoupon(command) }
            }

            @Test
            fun `남은 수량이 없으면 예외가 발생한다`() {
                val userId = "USER1"
                val name = "SOLD_OUT_COUPON"
                val quantity = 0
                val savedCoupon =
                    couponRepository.save(Coupon.create(name, quantity))
                val command = CouponIssueCommand.of(userId, savedCoupon.id.value)

                assertThrows<CouponOutOfStockException> { couponUseCase.issueCoupon(command) }
            }

            @Test
            fun `만료된 쿠폰이면 예외가 발생한다`() {
                val userId = "USER1"
                val name = "TEST_COUPON"
                val quantity = 10
                val savedCoupon =
                    couponRepository.save(Coupon.create(name, quantity, LocalDateTime.now().minusDays(1)))
                val command = CouponIssueCommand.of(userId, savedCoupon.id.value)

                assertThrows<CouponExpiredException> { couponUseCase.issueCoupon(command) }
            }

            @Test
            fun `이미 발급된 쿠폰이면 예외가 발생한다`() {
                val userId = "USER1"
                val name = "TEST_COUPON"
                val quantity = 10
                val savedCoupon = couponRepository.save(Coupon.create(name, quantity))
                userCouponRepository.issueCouponTo(userId, savedCoupon)
                val command = CouponIssueCommand.of(userId, savedCoupon.id.value)

                assertThrows<CouponAlreadyIssuedException> { couponUseCase.issueCoupon(command) }
            }

            @Test
            fun `성공`() {
                val userId = "USER1"
                val name = "TEST_COUPON"
                val quantity = 10
                val savedCoupon = couponRepository.save(Coupon.create(name, quantity))
                val command = CouponIssueCommand.of(userId, savedCoupon.id.value)

                val result = couponUseCase.issueCoupon(command)

                assertThat(result).isNotNull()
                assertThat(result.couponId).isEqualTo(savedCoupon.id.value)
                assertThat(result.userId).isEqualTo(userId)
                assertThat(result.couponStatus).isEqualTo(CouponStatus.ISSUED.name)

                // 쿠폰 수량 감소 확인
                val coupon = couponRepository.findById(result.couponId)
                assertThat(coupon!!.quantity.value).isEqualTo(quantity - 1)
            }
        }
    }
