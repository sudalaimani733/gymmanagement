package com.gym.gymmanagement.service;

import com.gym.gymmanagement.entity.Member;
import com.gym.gymmanagement.entity.Payment;
import com.gym.gymmanagement.repository.MemberRepository;
import com.gym.gymmanagement.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepo;
    private final MemberRepository memberRepo;

    public PaymentService(PaymentRepository paymentRepo, MemberRepository memberRepo) {
        this.paymentRepo = paymentRepo;
        this.memberRepo = memberRepo;
    }

    // ✅ ADD PAYMENT — checks for existing PENDING record first
    public Payment addPayment(Long memberId, Payment payment) {
        Member member = memberRepo.findById(memberId).orElse(null);
        if (member == null) return null;

        LocalDate now = LocalDate.now();
        int month = now.getMonthValue();
        int year = now.getYear();

        // Check if PENDING already exists for this member this month
        Optional<Payment> existing = paymentRepo.findPendingByMemberAndMonth(memberId, month, year);
        if (existing.isPresent()) {
            // Update the existing PENDING record instead of creating new
            Payment p = existing.get();
            p.setAmount(payment.getAmount());
            p.setStatus(payment.getStatus().toUpperCase());
            if ("PAID".equals(payment.getStatus().toUpperCase())) {
                p.setPaymentDate(now);
                p.setExpiryDate(now.plusDays(member.getPlan().getDuration()));
            }
            return paymentRepo.save(p);
        }

        // No existing record — create new
        payment.setMember(member);
        payment.setPaymentDate(now);
        int duration = member.getPlan().getDuration();
        payment.setExpiryDate(now.plusDays(duration));
        payment.setStatus(payment.getStatus().toUpperCase());
        return paymentRepo.save(payment);
    }

    // ✅ MARK AS PAID — updates PENDING → PAID
    public Payment markAsPaid(Long paymentId) {
        Payment payment = paymentRepo.findById(paymentId).orElse(null);
        if (payment == null) return null;

        LocalDate now = LocalDate.now();
        payment.setStatus("PAID");
        payment.setPaymentDate(now);

        int duration = payment.getMember().getPlan().getDuration();
        payment.setExpiryDate(now.plusDays(duration));

        return paymentRepo.save(payment);
    }

    // ✅ AUTO-GENERATE MONTHLY PENDING — one per member, skips if already exists
    public int generateMonthlyPending(int month, int year) {
        List<Member> allMembers = memberRepo.findAll();
        int created = 0;

        for (Member member : allMembers) {
            if (member.getPlan() == null) continue;

            boolean exists = paymentRepo.existsByMemberAndMonth(member.getId(), month, year);
            if (exists) continue;

            Payment p = new Payment();
            p.setMember(member);
            p.setAmount(member.getPlan().getPrice());
            p.setStatus("PENDING");
            p.setPaymentDate(LocalDate.of(year, month, 1));
            p.setExpiryDate(LocalDate.of(year, month, 1).plusDays(member.getPlan().getDuration()));
            paymentRepo.save(p);
            created++;
        }

        return created;
    }

    // ✅ GET PAYMENTS BY MONTH/YEAR
    public List<Payment> getPaymentsByMonth(int year, int month) {
        return paymentRepo.findByMonthAndYear(month, year);
    }

    // GET ALL PAYMENTS
    public List<Payment> getAllPayments() {
        return paymentRepo.findAll();
    }

    // GET BY ID
    public Payment getPaymentById(Long id) {
        return paymentRepo.findById(id).orElse(null);
    }

    // DELETE
    public void deletePayment(Long id) {
        paymentRepo.deleteById(id);
    }

    // MONTHLY REVENUE
    public Double getMonthlyRevenue(int month, int year) {
        Double total = paymentRepo.getMonthlyRevenue(month, year);
        return total != null ? total : 0.0;
    }

    public List<Payment> getPaymentsByStatus(String status) {
        return paymentRepo.findByStatus(status);
    }

    public List<Member> getExpiredMembers() {
        return paymentRepo.getExpiredMembers();
    }
}