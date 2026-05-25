package com.example.contractclosingworker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ContractClosingWorkerApplication {

    private static final Logger log = LoggerFactory.getLogger(ContractClosingWorkerApplication.class);

    public static void main(String[] args) {
        log.info("Contract Closing & Commission Settlement Worker is starting...");
        SpringApplication.run(ContractClosingWorkerApplication.class, args);
    }
}
