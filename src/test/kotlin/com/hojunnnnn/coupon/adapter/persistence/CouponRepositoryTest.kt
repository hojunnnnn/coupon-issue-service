package com.hojunnnnn.coupon.adapter.persistence

import com.hojunnnnn.coupon.adapter.persistence.entity.CouponEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class CouponRepositoryTest
    @Autowired
    constructor(
        val couponJpaRepository: CouponJpaRepository,
    ) {
        @Test
        fun `쿠폰을 생성할 수 있다`() {
            // given
            val couponEntity =
                CouponEntity(
                    name = "testCoupon",
                    quantity = 100,
                )

            // when
            val savedCoupon = couponJpaRepository.save(couponEntity)

            // then
            assertThat(savedCoupon).isNotNull()
        }
    }
