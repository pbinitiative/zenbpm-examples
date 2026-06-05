package eu.bpm4.bankloanworker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.zenbpm.grpc.JobContext;
import org.zenbpm.grpc.JobWorker;

/**
 * Validates the loan application data and creates an internal ticket in the
 * bank's IT systems. In production this would integrate with a ticketing API.
 */
@Component
public class ValidateDataWorker {

    private static final Logger log = LoggerFactory.getLogger(ValidateDataWorker.class);

    @JobWorker("validate_data")
    public void handle(JobContext ctx) {
        log.info("Validating loan application data and creating ticket. Job '{}', variables: {}", ctx.getWaitingJob().getKey(), ctx.getVariables());
    }
}
