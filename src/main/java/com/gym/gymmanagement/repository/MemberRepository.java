package com.gym.gymmanagement.repository;


import com.gym.gymmanagement.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;



    public interface MemberRepository extends JpaRepository<Member, Long> {
    }



