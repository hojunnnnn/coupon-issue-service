package com.hojunnnnn.coupon.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.LocalDateTime

@Entity
class Coupon(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val name: String,

    var quantity: Int,

    val createdDateTime: LocalDateTime = LocalDateTime.now(),

    val expiredDateTime: LocalDateTime = LocalDateTime.now().plusDays(7),
) {

}