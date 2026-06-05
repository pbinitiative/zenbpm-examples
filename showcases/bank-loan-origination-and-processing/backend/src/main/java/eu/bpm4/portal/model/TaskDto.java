package eu.bpm4.portal.model;

import java.time.Instant;
import java.util.Map;

public record TaskDto(
        String key,
        String elementId,
        String name,
        String processInstanceKey,
        String assignee,
        Instant createdAt,
        Map<String, Object> variables
) {}
