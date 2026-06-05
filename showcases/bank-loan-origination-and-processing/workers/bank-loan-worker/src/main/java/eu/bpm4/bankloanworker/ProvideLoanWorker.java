package eu.bpm4.bankloanworker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.zenbpm.grpc.JobContext;
import org.zenbpm.grpc.JobWorker;

/**
 * Triggers the bank's core banking system to disburse the approved loan funds
 * to the customer's account.
 */
@Component
public class ProvideLoanWorker {

    private static final Logger log = LoggerFactory.getLogger(ProvideLoanWorker.class);

    @JobWorker("provideLoan")
    public void handle(JobContext ctx) {
        log.info("Disbursing loan funds. Job '{}', variables: {}", ctx.getWaitingJob().getKey(), ctx.getVariables());
    }
}
