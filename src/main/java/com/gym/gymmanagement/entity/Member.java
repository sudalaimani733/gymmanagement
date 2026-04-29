package com.gym.gymmanagement.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int age;
    private String phone;

    @ManyToOne
    @JoinColumn(name = "plan_id")
    private Plan plan;
    @OneToMany(mappedBy = "member")
    @JsonIgnore
    private List<Payment> payments;
}