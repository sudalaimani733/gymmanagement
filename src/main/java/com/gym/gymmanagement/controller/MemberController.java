package com.gym.gymmanagement.controller;

import com.gym.gymmanagement.entity.Member;
import com.gym.gymmanagement.service.MemberService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/members")
@CrossOrigin(origins = "*")
public class MemberController {

    private final MemberService service;

    public MemberController(MemberService service) {
        this.service = service;
    }

    @PostMapping
    public Member addMember(@RequestBody Member member) {
        return service.addMember(member);
    }

    @GetMapping
    public List<Member> getAllMembers() {
        return service.getAllMembers();
    }

    @GetMapping("/{id}")
    public Member getMember(@PathVariable Long id) {
        return service.getMemberById(id);
    }

    @PutMapping("/{id}")
    public Member updateMember(@PathVariable Long id, @RequestBody Member member) {
        return service.updateMember(id, member);
    }

    @DeleteMapping("/{id}")
    public String deleteMember(@PathVariable Long id) {
        service.deleteMember(id);
        return "Deleted Successfully";
    }
}