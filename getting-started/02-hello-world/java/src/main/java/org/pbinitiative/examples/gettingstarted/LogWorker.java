package org.pbinitiative.examples.gettingstarted;

import org.springframework.stereotype.Component;
import org.zenbpm.grpc.JobWorker;
import org.zenbpm.grpc.JobContext;

import java.util.Map;

/**
 * Handles the "log-worker" service task of the hello-world process.
 *
 * When a process instance reaches the "Log Greeting" task, the engine creates a
 * job of type "log-worker" and waits. This method picks it up, reads the "log"
 * variable, prints it, and completes the job so the instance can finish.
 *
 * verify: JobWorker/JobContext package names against the zenbpm-java-client release.
 */
@Component
public class LogWorker {

    @JobWorker("log-worker")
    public Map<String, Object> handleJob(JobContext ctx) {
        System.out.println("[log-worker] " + ctx.getVariables().get("log"));
        return Map.of(); // no output variables; job complete
    }
}
