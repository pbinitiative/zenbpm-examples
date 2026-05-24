package eu.bpm4.portal.zenbpm.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProcessDefinitionResponse {
    private String key;
    private String bpmnData;
}
