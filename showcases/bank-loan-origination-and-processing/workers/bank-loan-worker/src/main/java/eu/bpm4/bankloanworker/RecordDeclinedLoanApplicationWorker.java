package eu.bpm4.bankloanworker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.zenbpm.grpc.JobContext;
import org.zenbpm.grpc.JobWorker;

/**
 * Records the declined loan application in the bank's IT systems for
 * compliance and audit purposes.
 */
@Component
public class RecordDeclinedLoanApplicationWorker {

    private static final Logger log = LoggerFactory.getLogger(RecordDeclinedLoanApplicationWorker.class);

    @JobWorker("recordDeclinedLoanApplication")
    public void handle(JobContext ctx) {
        log.info("Recording declined loan application. Job '{}', variables: {}", ctx.getWaitingJob().getKey(), ctx.getVariables());
    }
}
