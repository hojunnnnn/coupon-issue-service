package com.hojunnnnn.coupon.application.service

import com.hojunnnnn.coupon.adapter.persistence.CouponJpaRepository
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
import java.time.LocalDateTime

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

/**
 * 쿠폰 유스케이스 인터페이스
 * 헥사고날 아키텍처의 인바운드 포트
 */
interface CouponUseCase {
    fun createCoupon(name: String, quantity: Int): CouponCreateResponse
}


/**
 * 쿠폰 관련 비즈니스 로직 처리 서비스
 * 헥사고날 아키텍처의 애플리케이션 서비스
 */
class CouponService(
    val couponRepository: CouponRepository,
) : CouponUseCase {

    override fun createCoupon(name: String, quantity: Int): CouponCreateResponse {
        if(couponRepository.existsByName(name)) { throw Exception() }
        val savedCoupon = couponRepository.save(Coupon(name = name, quantity = quantity))
        return CouponCreateResponse(
            id = savedCoupon.id,
            name = savedCoupon.name,
            quantity = savedCoupon.quantity,
            expiredDateTime = savedCoupon.expiredDateTime,
        )
    }

}

/**
 * 쿠폰 데이터 접근을 위한 인터페이스
 * 헥사고날 아키텍처의 아웃바운드 포트
 */
interface CouponRepository {

    fun save(coupon: Coupon): Coupon

    fun existsByName(name: String): Boolean

}

/**
 * JPA 쿠폰 데이터 접근 구현체
 * 헥사고날 아키텍처의 아웃바운드 어댑터
 */
class CouponPersistenceAdapter(
    val couponJpaRepository: CouponJpaRepository,
) : CouponRepository {

    // domain <--> entity 매핑 구현 생략
    override fun save(coupon: Coupon): Coupon {
        return couponJpaRepository.save(coupon)
    }

    override fun existsByName(name: String): Boolean {
        return couponJpaRepository.existsByName(name)
    }

}

data class CouponCreateResponse(
    val id: Long,
    val name: String,
    val quantity: Int,
    val expiredDateTime: LocalDateTime,
)
