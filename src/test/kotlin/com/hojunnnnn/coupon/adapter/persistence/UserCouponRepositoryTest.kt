package com.hojunnnnn.coupon.adapter.persistence

import com.hojunnnnn.coupon.application.port.out.UserCouponRepository
import com.hojunnnnn.coupon.adapter.persistence.entity.CouponEntity
import com.hojunnnnn.coupon.adapter.persistence.mapper.UserCouponPersistenceMapper
import com.hojunnnnn.coupon.domain.Coupon
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class UserCouponRepositoryTest {
    @Autowired private lateinit var userCouponJpaRepository: UserCouponJpaRepository

    private lateinit var userCouponRepository: UserCouponRepository

    @BeforeEach
    fun init() {
        userCouponRepository = UserCouponPersistenceAdapter(userCouponJpaRepository, UserCouponPersistenceMapper())
    }

    @Test
    fun `유저는 쿠폰을 발급 받을 수 있다`() {
        // given
        val userId = "user1"
        val coupon = Coupon.create("TEST_COUPON", 10)


        // when
        val issuedCoupon = userCouponRepository.issueCouponTo(userId, coupon)

        // then
        assertThat(issuedCoupon).isNotNull()
        assertThat(issuedCoupon.couponId).isEqualTo(coupon.id)
    }
}
