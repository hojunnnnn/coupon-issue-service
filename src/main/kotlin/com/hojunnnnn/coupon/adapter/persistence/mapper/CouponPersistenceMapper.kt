package com.hojunnnnn.coupon.adapter.persistence.mapper

import com.hojunnnnn.coupon.adapter.persistence.entity.CouponEntity
import com.hojunnnnn.coupon.domain.Coupon
import com.hojunnnnn.coupon.domain.CouponExpirationDays
import com.hojunnnnn.coupon.domain.CouponId
import com.hojunnnnn.coupon.domain.CouponName
import com.hojunnnnn.coupon.domain.CouponQuantity
import org.springframework.stereotype.Component

@Component
class CouponPersistenceMapper {
    fun toEntity(coupon: Coupon): CouponEntity =
        CouponEntity(
            id = coupon.id.value,
            name = coupon.name.value,
            quantity = coupon.quantity.value,
            createdDateTime = coupon.createdDateTime,
            expiredDateTime = coupon.expiredDateTime.value,
        )

    fun toDomain(couponEntity: CouponEntity): Coupon =
        Coupon(
            id = CouponId(couponEntity.id),
            name = CouponName(couponEntity.name),
            quantity = CouponQuantity(couponEntity.quantity),
            createdDateTime = couponEntity.createdDateTime,
            expiredDateTime = CouponExpirationDays(couponEntity.expiredDateTime),
        )
}
