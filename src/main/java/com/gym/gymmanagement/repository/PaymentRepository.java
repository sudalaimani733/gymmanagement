package com.gym.gymmanagement.repository;

import java.util.List;
import java.util.Optional;
import com.gym.gymmanagement.entity.Member;
import com.gym.gymmanagement.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query("SELECT SUM(p.amount) FROM Payment p WHERE MONTH(p.paymentDate) = :month AND YEAR(p.paymentDate) = :year AND p.status = 'PAID'")
    Double getMonthlyRevenue(@Param("month") int month, @Param("year") int year);

    @Query("SELECT DISTINCT p.member FROM Payment p WHERE p.expiryDate < CURRENT_DATE")
    List<Member> getExpiredMembers();

    @Query("SELECT p FROM Payment p WHERE LOWER(p.status) = LOWER(:status)")
    List<Payment> findByStatus(@Param("status") String status);

    // ✅ NEW: Check if a PENDING payment already exists for member in a given month/year
    @Query("SELECT p FROM Payment p WHERE p.member.id = :memberId AND MONTH(p.paymentDate) = :month AND YEAR(p.paymentDate) = :year AND p.status = 'PENDING'")
    Optional<Payment> findPendingByMemberAndMonth(
            @Param("memberId") Long memberId,
            @Param("month") int month,
            @Param("year") int year
    );

    // ✅ NEW: Get all payments for a specific month/year
    @Query("SELECT p FROM Payment p WHERE MONTH(p.paymentDate) = :month AND YEAR(p.paymentDate) = :year")
    List<Payment> findByMonthAndYear(@Param("month") int month, @Param("year") int year);

    // ✅ NEW: Check if ANY payment exists for member in month/year (avoid duplicates)
    @Query("SELECT COUNT(p) > 0 FROM Payment p WHERE p.member.id = :memberId AND MONTH(p.paymentDate) = :month AND YEAR(p.paymentDate) = :year")
    boolean existsByMemberAndMonth(
            @Param("memberId") Long memberId,
            @Param("month") int month,
            @Param("year") int year
    );
}