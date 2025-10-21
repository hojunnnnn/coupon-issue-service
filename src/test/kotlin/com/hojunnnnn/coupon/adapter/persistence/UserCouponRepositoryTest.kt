package com.hojunnnnn.coupon.adapter.persistence

import com.hojunnnnn.coupon.application.port.out.CouponRepository
import com.hojunnnnn.coupon.domain.Coupon
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@DataJpaTest
class UserCouponRepositoryTest {

    @Autowired private lateinit var couponJpaRepository: CouponJpaRepository
    @Autowired private lateinit var userCouponJpaRepository: UserCouponJpaRepository

    private lateinit var couponRepository: CouponRepository
    private lateinit var userCouponRepository: UserCouponRepository

    @BeforeEach
    fun init() {
        couponRepository = CouponPersistenceAdapter(couponJpaRepository)
        userCouponRepository = UserCouponPersistenceAdapter(userCouponJpaRepository)
    }

    @Test
    fun `유저는 쿠폰을 발급 받을 수 있다`() {
        // given
        val userId = "user1"
        val savedCoupon = couponRepository.save(Coupon(
            name = "TEST_COUPON",
            quantity = 10
        ))

        // when
        val issuedCoupon = userCouponRepository.issueCouponTo(userId, savedCoupon)

        // then
        assertThat(issuedCoupon).isNotNull()
        assertThat(issuedCoupon.couponId).isEqualTo(savedCoupon.id)
    }

}

interface UserCouponRepository {

    fun issueCouponTo(userId: String, coupon: Coupon): UserCoupon
}

interface UserCouponJpaRepository : JpaRepository<UserCoupon, Long> {

}

@Component
class UserCouponPersistenceAdapter(
    private val userCouponJpaRepository: UserCouponJpaRepository,
) : UserCouponRepository {

    override fun issueCouponTo(
        userId: String,
        coupon: Coupon
    ): UserCoupon {
        val userCoupon = UserCoupon(
            couponId = coupon.id,
            userId = userId,
            status = CouponStatus.ISSUED,
        )
        return userCouponJpaRepository.save(userCoupon)
    }

}

@Entity
class UserCoupon(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @Column(nullable = false)
    val couponId: Long,
    @Column(nullable = false)
    val userId: String,
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val status: CouponStatus,
    @Column(nullable = false)
    val issuedDateTime: LocalDateTime = LocalDateTime.now(),
) {

}

enum class CouponStatus {
    ISSUED,
    USED,
    EXPIRED
}

