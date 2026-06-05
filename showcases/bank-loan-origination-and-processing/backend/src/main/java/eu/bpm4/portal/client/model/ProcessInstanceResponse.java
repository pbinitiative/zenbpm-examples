package eu.bpm4.portal.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProcessInstanceResponse {
    private String key;
    private String processDefinitionKey;
    private String bpmnProcessId;
    private String state;
}
