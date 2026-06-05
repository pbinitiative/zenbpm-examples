package eu.bpm4.bankloanworker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.zenbpm.grpc.JobContext;
import org.zenbpm.grpc.JobWorker;

/**
 * Sends an email notification to the customer.
 * The {@code type} input variable determines the template to use:
 * {@code offer}, {@code rejection}, or {@code confirmation}.
 */
@Component
public class EmailNotificationWorker {

    private static final Logger log = LoggerFactory.getLogger(EmailNotificationWorker.class);

    @JobWorker("email_notification")
    public void handle(JobContext ctx) {
        log.info("Sending email notification. Job '{}', variables: {}", ctx.getWaitingJob().getKey(), ctx.getVariables());
    }
}
