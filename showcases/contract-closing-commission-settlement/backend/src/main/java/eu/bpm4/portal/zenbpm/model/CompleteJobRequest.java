package eu.bpm4.portal.zenbpm.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class CompleteJobRequest {

    private Map<String, Object> variables;
}
