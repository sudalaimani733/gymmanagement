
package com.gym.gymmanagement.service;

import com.gym.gymmanagement.entity.Plan;
import com.gym.gymmanagement.repository.PlanRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanService {

    private final PlanRepository repo;

    public PlanService(PlanRepository repo) {
        this.repo = repo;
    }

    public Plan addPlan(Plan plan) {
        return repo.save(plan);
    }

    public List<Plan> getAllPlans() {
        return repo.findAll();
    }

    public Plan updatePlan(Long id, Plan newPlan) {
        Plan p = repo.findById(id).orElse(null);
        if (p != null) {
            p.setName(newPlan.getName());
            p.setPrice(newPlan.getPrice());
            p.setDuration(newPlan.getDuration());
            return repo.save(p);
        }
        return null;
    }

    public void deletePlan(Long id) {
        repo.deleteById(id);
    }
}