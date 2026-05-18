package com.example.contractonboardingworker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.zenbpm.grpc.JobContext;
import org.zenbpm.grpc.JobWorker;

@Component
public class SendCommissionInformationWorker {

    private static final Logger log = LoggerFactory.getLogger(SendCommissionInformationWorker.class);

    @JobWorker("send-commission-information")
    public void handle(JobContext ctx) {
        log.info("Handling job '{}', variables: {}", ctx.getWaitingJob().getKey(), ctx.getVariables());
    }
}
