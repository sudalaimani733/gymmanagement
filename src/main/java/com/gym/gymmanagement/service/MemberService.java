package com.gym.gymmanagement.service;

import com.gym.gymmanagement.entity.Member;
import com.gym.gymmanagement.entity.Plan;
import com.gym.gymmanagement.repository.MemberRepository;
import com.gym.gymmanagement.repository.PlanRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {

    private final MemberRepository repo;
    private final PlanRepository planRepo;

    public MemberService(MemberRepository repo, PlanRepository planRepo) {
        this.repo = repo;
        this.planRepo = planRepo;
    }

    // ✅ CREATE MEMBER
    public Member addMember(Member member) {

        if (member.getPlan() != null) {
            Long planId = member.getPlan().getId();

            Plan plan = planRepo.findById(planId)
                    .orElseThrow(() -> new RuntimeException("Plan not found"));

            member.setPlan(plan); // 🔥 set full plan object
        }

        return repo.save(member);
    }

    // ✅ GET ALL
    public List<Member> getAllMembers() {
        return repo.findAll();
    }

    // ✅ GET BY ID
    public Member getMemberById(Long id) {
        return repo.findById(id).orElse(null);
    }

    // ✅ UPDATE MEMBER
    public Member updateMember(Long id, Member newMember) {
        Member m = repo.findById(id).orElse(null);

        if (m != null) {
            m.setName(newMember.getName());
            m.setAge(newMember.getAge());
            m.setPhone(newMember.getPhone());

            // 🔥 IMPORTANT FIX
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

    // ✅ DELETE
    public void deleteMember(Long id) {
        repo.deleteById(id);
    }
}