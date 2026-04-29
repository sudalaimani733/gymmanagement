package com.gym.gymmanagement.controller;

import com.gym.gymmanagement.entity.Plan;
import com.gym.gymmanagement.service.PlanService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/plans")
@CrossOrigin(origins = "*")
public class PlanController {

    private final PlanService service;

    public PlanController(PlanService service) {
        this.service = service;
    }

    @PostMapping
    public Plan addPlan(@RequestBody Plan plan) {
        return service.addPlan(plan);
    }

    @GetMapping
    public List<Plan> getAllPlans() {
        return service.getAllPlans();
    }

    @PutMapping("/{id}")
    public Plan updatePlan(@PathVariable Long id, @RequestBody Plan plan) {
        return service.updatePlan(id, plan);
    }

    @DeleteMapping("/{id}")
    public String deletePlan(@PathVariable Long id) {
        service.deletePlan(id);
        return "Deleted Successfully";
    }
}
