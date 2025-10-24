package com.hojunnnnn.coupon.application.concurrency

import com.hojunnnnn.coupon.application.port.out.CouponRepository
import com.hojunnnnn.coupon.application.service.CouponIssuer
import com.hojunnnnn.coupon.application.service.CouponService
import com.hojunnnnn.coupon.domain.Coupon
import com.hojunnnnn.coupon.domain.CouponStatus
import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import kotlin.test.Test

@SpringBootTest
class CouponServiceConcurrencyTest {
    @Autowired
    private lateinit var couponService: CouponService

    @Autowired
    private lateinit var couponRepository: CouponRepository

    @Autowired
    private lateinit var couponIssuer: CouponIssuer

    /**"락 없이 쿠폰 발급시 동일 유저 중복 발급이 DB 제약조건으로 방어되는지 검증" */
    @Test
    fun `동일한 유저가 따닥 요청해도 하나만 발급된다`() {
        val name = "SUMMER_COUPON"
        val quantity = 10
        val userId = "USER1"

        val numberOfThread = 2
        val executorService = Executors.newFixedThreadPool(numberOfThread)
        val successfulIssuance = AtomicInteger(0)
        val failedIssuance = AtomicInteger(0)

        val savedCoupon = couponRepository.save(Coupon.create(name, quantity))

        repeat(numberOfThread) {
            executorService.submit {
                try {
                    couponIssuer.issueCoupon(userId, savedCoupon.id.value)
                    successfulIssuance.incrementAndGet()
                } catch (e: Exception) {
                    failedIssuance.incrementAndGet()
                }
            }
        }

        executorService.shutdown()
        executorService.awaitTermination(1, TimeUnit.SECONDS)

        assertThat(successfulIssuance.get()).isEqualTo(1)
        assertThat(failedIssuance.get()).isEqualTo(1)
        println("successfulIssuance : ${successfulIssuance.get()}")
        println("failedIssuance : ${failedIssuance.get()}")

        val coupon = couponRepository.findById(savedCoupon.id.value)
        assertThat(coupon.quantity.value).isEqualTo(9)
    }

    @Test
    fun `동시에 쿠폰 발급 요청이 들어와도 수량만큼만 발급된다`() {
        // given
        val name = "WINTER_COUPON"
        val quantity = 100

        val numberOfThread = 1000
        val executorService = Executors.newFixedThreadPool(numberOfThread)
        val latch = CountDownLatch(numberOfThread)
        val successfulIssuance = AtomicInteger(0)
        val failedIssuance = AtomicInteger(0)

        val userIds = (1..numberOfThread).map { "user-$it" }
        val savedCoupon = couponRepository.save(Coupon.create(name, quantity))

        // when
        repeat(numberOfThread) { index ->
            executorService.submit {
                try {
                    val userId = userIds[index]

                    val result = couponService.issueCoupon(userId, savedCoupon.id.value)
                    if (CouponStatus.ISSUED.name == result.couponStatus) {
                        successfulIssuance.incrementAndGet()
                    } else {
                        failedIssuance.incrementAndGet()
                    }
                } catch (e: Exception) {
                    failedIssuance.incrementAndGet()
                } finally {
                    latch.countDown()
                }
            }
        }
        latch.await()

        // then
        assertThat(successfulIssuance.get()).isEqualTo(100)
        assertThat(failedIssuance.get()).isEqualTo(900)
        println("successfulIssuance : ${successfulIssuance.get()}")
        println("failedIssuance : ${failedIssuance.get()}")

        val coupon = couponRepository.findById(savedCoupon.id.value)
        assertThat(coupon.quantity.value).isEqualTo(0)
    }
}
