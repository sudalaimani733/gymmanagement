package com.gym.gymmanagement.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private double price;
    private int duration; // in days

    @OneToMany(mappedBy = "plan")
    @JsonIgnore   // 🔥 prevents infinite loop
    private List<Member> members;
}