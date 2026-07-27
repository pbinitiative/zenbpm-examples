package org.pbinitiative.examples.gettingstarted;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Boots the worker. The ZenBPM Spring Boot starter connects to the engine over
 * gRPC on startup (see application.yml) and registers every @JobWorker bean.
 */
@SpringBootApplication
public class FirstBpmnProcessWorkerApplication {
    public static void main(String[] args) {
        SpringApplication.run(FirstBpmnProcessWorkerApplication.class, args);
    }
}
