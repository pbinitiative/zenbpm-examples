package eu.bpm4.bankloanworker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BankLoanWorkerApplication {

    private static final Logger log = LoggerFactory.getLogger(BankLoanWorkerApplication.class);

    public static void main(String[] args) {
        log.info("Bank Loan Origination and Processing Worker is starting...");
        SpringApplication.run(BankLoanWorkerApplication.class, args);
    }
}
