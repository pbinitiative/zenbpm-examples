package eu.bpm4.portal.zenbpm.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class JobResponse {

    private String key;
    private String elementId;
    private String processInstanceKey;
    private String assignee;
    private Long createdAt;
    private String type;
    private Map<String, Object> variables;
}
