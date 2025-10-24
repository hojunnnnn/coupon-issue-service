package com.hojunnnnn.coupon.adapter.persistence.mapper

import com.hojunnnnn.coupon.adapter.persistence.entity.UserCouponEntity
import com.hojunnnnn.coupon.domain.CouponId
import com.hojunnnnn.coupon.domain.CouponStatus
import com.hojunnnnn.coupon.domain.UserCoupon
import com.hojunnnnn.coupon.domain.UserCouponId
import com.hojunnnnn.coupon.domain.UserId
import org.springframework.stereotype.Component

@Component
class UserCouponPersistenceMapper {
    fun toEntity(userCoupon: UserCoupon): UserCouponEntity =
        UserCouponEntity(
            id = userCoupon.id.value,
            userId = userCoupon.userId.value,
            couponId = userCoupon.couponId.value,
            status = userCoupon.status.name,
            issuedDateTime = userCoupon.issuedDateTime,
        )

    fun toDomain(userCouponEntity: UserCouponEntity): UserCoupon =
        UserCoupon(
            id = UserCouponId(userCouponEntity.id),
            userId = UserId(userCouponEntity.userId),
            couponId = CouponId(userCouponEntity.couponId),
            status = CouponStatus.valueOf(userCouponEntity.status),
            issuedDateTime = userCouponEntity.issuedDateTime,
        )
}
