package com.gym.gymmanagement.service;

import com.gym.gymmanagement.entity.Member;
import com.gym.gymmanagement.entity.Payment;
import com.gym.gymmanagement.entity.Plan;
import com.gym.gymmanagement.repository.MemberRepository;
import com.gym.gymmanagement.repository.PaymentRepository;
import com.gym.gymmanagement.repository.PlanRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MemberService {

    private final MemberRepository repo;
    private final PlanRepository planRepo;
    private final PaymentRepository paymentRepo;

    public MemberService(MemberRepository repo, PlanRepository planRepo, PaymentRepository paymentRepo) {
        this.repo = repo;
        this.planRepo = planRepo;
        this.paymentRepo = paymentRepo;
    }

    public Member addMember(Member member) {
        if (member.getPlan() != null) {
            Long planId = member.getPlan().getId();
            Plan plan = planRepo.findById(planId)
                    .orElseThrow(() -> new RuntimeException("Plan not found"));
            member.setPlan(plan);
        }

        // Use provided joining date or fallback to today
        Member saved = repo.save(member);

        // Auto-create payment when member is added
        if (saved.getPlan() != null) {
            LocalDate joiningDate = member.getJoiningDate() != null
                    ? member.getJoiningDate()
                    : LocalDate.now();

            Payment payment = new Payment();
            payment.setMember(saved);
            payment.setAmount(saved.getPlan().getPrice());
            payment.setPaymentDate(joiningDate);
            payment.setExpiryDate(joiningDate.plusDays(saved.getPlan().getDuration()));

            // Use provided status
            String status = member.getPaymentStatus() != null
                    ? member.getPaymentStatus().toUpperCase()
                    : "PENDING";
            payment.setStatus(status);

            paymentRepo.save(payment);
        }

        return saved;
    }

    public List<Member> getAllMembers() { return repo.findAll(); }

    public Member getMemberById(Long id) { return repo.findById(id).orElse(null); }

    public Member updateMember(Long id, Member newMember) {
        Member m = repo.findById(id).orElse(null);
        if (m != null) {
            m.setName(newMember.getName());
            m.setAge(newMember.getAge());
            m.setPhone(newMember.getPhone());
            if (newMember.getJoiningDate() != null) {
                m.setJoiningDate(newMember.getJoiningDate());
            }
            if (newMember.getPlan() != null) {
                Long planId = newMember.getPlan().getId();
                Plan plan = planRepo.findById(planId)
                        .orElseThrow(() -> new RuntimeException("Plan not found"));
                m.setPlan(plan);
            }
            return repo.save(m);
        }
        return null;
    }

    public void deleteMember(Long id) {
        // Delete all payments for this member first
        List<Payment> memberPayments = paymentRepo.findByMemberId(id);
        paymentRepo.deleteAll(memberPayments);
        // Then delete the member
        repo.deleteById(id);
    }
}