package com.gym.gymmanagement.scheduler;

import com.gym.gymmanagement.service.PaymentService;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@EnableScheduling
public class PaymentScheduler {

    private final PaymentService paymentService;

    public PaymentScheduler(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // Runs at 00:01 on the 1st day of every month
    @Scheduled(cron = "0 1 0 1 * *")
    public void generateMonthlyPendingPayments() {
        LocalDate now = LocalDate.now();
        int count = paymentService.generateMonthlyPending(now.getMonthValue(), now.getYear());
        System.out.println("[Scheduler] Auto-generated " + count + " pending payments for "
                + now.getMonth() + " " + now.getYear());
    }
}