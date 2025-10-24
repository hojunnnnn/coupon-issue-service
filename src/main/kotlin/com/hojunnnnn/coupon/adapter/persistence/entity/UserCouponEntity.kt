package com.hojunnnnn.coupon.adapter.persistence.entity

import jakarta.persistence.*
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
class UserCouponEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @Column(name = "coupon_id", nullable = false)
    val couponId: Long,
    @Column(name = "user_id", nullable = false)
    val userId: String,
    @Column(nullable = false)
    val status: String = "ISSUED",
    @Column(nullable = false)
    val issuedDateTime: LocalDateTime = LocalDateTime.now(),
)
