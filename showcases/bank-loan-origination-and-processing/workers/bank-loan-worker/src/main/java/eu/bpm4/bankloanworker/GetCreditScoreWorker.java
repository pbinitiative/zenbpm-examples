package eu.bpm4.bankloanworker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.zenbpm.grpc.JobContext;
import org.zenbpm.grpc.JobWorker;

/**
 * Simulates a call to a FICO agency to retrieve the applicant's credit score.
 * In production this would call an external credit bureau API.
 */
@Component
public class GetCreditScoreWorker {

    private static final Logger log = LoggerFactory.getLogger(GetCreditScoreWorker.class);

    @JobWorker("getCreditScore")
    public void handle(JobContext ctx) {
        log.info("Fetching credit score. Job '{}', variables: {}", ctx.getWaitingJob().getKey(), ctx.getVariables());
    }
}
