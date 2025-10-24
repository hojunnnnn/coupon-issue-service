package com.hojunnnnn.coupon.adapter.persistence

import com.hojunnnnn.coupon.adapter.persistence.mapper.UserCouponPersistenceMapper
import com.hojunnnnn.coupon.application.port.out.UserCouponRepository
import com.hojunnnnn.coupon.domain.Coupon
import com.hojunnnnn.coupon.domain.UserCoupon
import org.springframework.stereotype.Component

@Component
class UserCouponPersistenceAdapter(
    private val userCouponJpaRepository: UserCouponJpaRepository,
    private val userCouponPersistenceMapper: UserCouponPersistenceMapper,
) : UserCouponRepository {
    override fun issueCouponTo(
        userId: String,
        coupon: Coupon,
    ): UserCoupon {
        val entity = userCouponPersistenceMapper.toEntity(UserCoupon.create(coupon.id.value, userId))
        val savedEntity = userCouponJpaRepository.save(entity)
        return userCouponPersistenceMapper.toDomain(savedEntity)
    }

    override fun isAlreadyIssuedCoupon(
        userId: String,
        couponId: Long,
    ): Boolean = userCouponJpaRepository.existsByUserIdAndCouponId(userId, couponId)
}
