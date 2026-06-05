package eu.bpm4.bankloanworker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.zenbpm.grpc.JobContext;
import org.zenbpm.grpc.JobWorker;

/**
 * Sends a message to the bank chatbot on behalf of the process.
 * The {@code type} input variable distinguishes the message kind
 * (e.g. {@code approval_and_offer} or {@code rejection}).
 */
@Component
public class ChatbotMessageWorker {

    private static final Logger log = LoggerFactory.getLogger(ChatbotMessageWorker.class);

    @JobWorker("chatbot_message")
    public void handle(JobContext ctx) {
        log.info("Sending chatbot message. Job '{}', variables: {}", ctx.getWaitingJob().getKey(), ctx.getVariables());
    }
}
