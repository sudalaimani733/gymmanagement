package com.gym.gymmanagement.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double amount;

    private LocalDate paymentDate;
    private LocalDate expiryDate;

    private String status; // PAID or PENDING

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
}