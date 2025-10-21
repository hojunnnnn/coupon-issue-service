package com.hojunnnnn.coupon.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.LocalDateTime

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