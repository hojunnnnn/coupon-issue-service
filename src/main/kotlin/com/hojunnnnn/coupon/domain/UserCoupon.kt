package com.hojunnnnn.coupon.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import java.time.LocalDateTime

@Table(
    uniqueConstraints = [
        UniqueConstraint(
            name = "uk_user_coupon_user_id_coupon_id",
            columnNames = ["user_id", "coupon_id"],
        ),
    ],
)
@Entity
class UserCoupon(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @Column(name = "coupon_id", nullable = false)
    val couponId: Long,
    @Column(name = "user_id", nullable = false)
    val userId: String,
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val status: CouponStatus,
    @Column(nullable = false)
    val issuedDateTime: LocalDateTime = LocalDateTime.now(),
)
