package com.gym.gymmanagement.controller;

import com.gym.gymmanagement.entity.Member;
import com.gym.gymmanagement.entity.Payment;
import com.gym.gymmanagement.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/payments")
@CrossOrigin(origins = "*")
public class PaymentController {

    private final PaymentService service;

    public PaymentController(PaymentService service) {
        this.service = service;
    }

    // ADD / UPDATE PAYMENT FOR MEMBER
    @PostMapping("/member/{memberId}")
    public Payment addPayment(@PathVariable Long memberId,
                              @RequestBody Payment payment) {
        return service.addPayment(memberId, payment);
    }

    // ✅ MARK PENDING → PAID
    @PutMapping("/{id}/mark-paid")
    public ResponseEntity<?> markAsPaid(@PathVariable Long id) {
        Payment updated = service.markAsPaid(id);
        if (updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(updated);
    }

    // ✅ AUTO-GENERATE MONTHLY PENDING RECORDS
    @PostMapping("/generate-monthly")
    public ResponseEntity<?> generateMonthly(@RequestBody(required = false) Map<String, Integer> body) {
        int month, year;
        if (body != null && body.containsKey("month") && body.containsKey("year")) {
            month = body.get("month");
            year = body.get("year");
        } else {
            LocalDate now = LocalDate.now();
            month = now.getMonthValue();
            year = now.getYear();
        }
        int count = service.generateMonthlyPending(month, year);
        return ResponseEntity.ok(Map.of(
                "created", count,
                "month", month,
                "year", year,
                "message", count + " pending records created"
        ));
    }

    // ✅ GET PAYMENTS BY MONTH/YEAR
    @GetMapping("/month/{year}/{month}")
    public List<Payment> getByMonth(@PathVariable int year, @PathVariable int month) {
        return service.getPaymentsByMonth(year, month);
    }

    // GET ALL
    @GetMapping
    public List<Payment> getAllPayments() {
        return service.getAllPayments();
    }

    // GET BY ID
    @GetMapping("/{id}")
    public Payment getPayment(@PathVariable Long id) {
        return service.getPaymentById(id);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public String deletePayment(@PathVariable Long id) {
        service.deletePayment(id);
        return "Deleted";
    }

    @GetMapping("/revenue/{year}/{month}")
    public Double getMonthlyRevenue(@PathVariable int year, @PathVariable int month) {
        return service.getMonthlyRevenue(month, year);
    }

    @GetMapping("/status/{status}")
    public List<Payment> getPaymentsByStatus(@PathVariable String status) {
        return service.getPaymentsByStatus(status.toUpperCase());
    }

    @GetMapping("/expired-members")
    public List<Member> getExpiredMembers() {
        return service.getExpiredMembers();
    }
}