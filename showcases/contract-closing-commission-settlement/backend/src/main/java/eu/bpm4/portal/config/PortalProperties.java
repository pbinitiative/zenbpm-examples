package eu.bpm4.portal.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "portal")
public class PortalProperties {

    private Cors cors = new Cors();
    private Process process = new Process();

    @Getter
    @Setter
    public static class Cors {
        private String[] allowedOrigins = {"http://localhost:5173"};
    }

    @Getter
    @Setter
    public static class Process {
        private String defaultKey = "my-process-key";
    }
}
