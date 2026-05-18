package eu.bpm4.portal.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "zenbpm")
public class ZenBpmProperties {

    private String baseUrl = "http://zenbpm:8080/v1";
}
