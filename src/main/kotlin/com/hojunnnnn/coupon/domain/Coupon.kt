package com.hojunnnnn.coupon.domain

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime

@Entity
class Coupon(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @Column(nullable = false, unique = true)
    val name: String,
    @Column(nullable = false)
    var quantity: Int,
    @CreationTimestamp
    @Column(nullable = false)
    val createdDateTime: LocalDateTime = LocalDateTime.now(),
    @Column(nullable = false)
    val expiredDateTime: LocalDateTime = LocalDateTime.now().plusDays(7),
) {
    fun isExpired(currentDateTime: LocalDateTime): Boolean = currentDateTime.isAfter(expiredDateTime)

    fun isSoldOut(): Boolean = quantity <= 0

    fun decreaseQuantity() {
        quantity -= 1
    }
}
