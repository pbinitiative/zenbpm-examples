package com.example.contractonboardingworker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ContractOnboardingWorkerApplication {

    private static final Logger log = LoggerFactory.getLogger(ContractOnboardingWorkerApplication.class);

    public static void main(String[] args) {
        log.info("Contract Onboarding Worker is starting...");
        SpringApplication.run(ContractOnboardingWorkerApplication.class, args);
    }
}
