package eu.bpm4.portal.model;

import java.util.Map;

public record StartProcessRequest(
        Map<String, Object> variables,
        String businessKey
) {}
