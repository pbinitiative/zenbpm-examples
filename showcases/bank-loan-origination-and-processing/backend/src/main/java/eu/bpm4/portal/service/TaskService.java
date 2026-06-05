package eu.bpm4.portal.service;

import eu.bpm4.portal.client.ZenBpmClient;
import eu.bpm4.portal.client.model.JobResponse;
import eu.bpm4.portal.client.model.ProcessDefinitionResponse;
import eu.bpm4.portal.client.model.ProcessInstanceResponse;
import eu.bpm4.portal.model.TaskDto;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private static final Logger log = LoggerFactory.getLogger(TaskService.class);

    private final ZenBpmClient zenBpmClient;

    /**
     * Directory to scan for form JSON files.
     * Each file must be named after the BPMN element ID it belongs to,
     * e.g. Activity_0092kbc.json.
     *
     * Configured via the PORTAL_FORMS_PATH environment variable (or
     * portal.forms.path application property). Accepts any Spring resource
     * prefix: "file:/app/forms/" for a volume-mounted directory or
     * "classpath:forms/" as the built-in fallback.
     */
    @Value("${portal.forms.path:classpath:forms/}")
    private String formsPath;

    /**
     * Map of BPMN elementId → form JSON string.
     * Populated at startup by scanning the configured forms directory.
     */
    private final Map<String, String> formsByElementId;

    public TaskService(ZenBpmClient zenBpmClient) {
        this.zenBpmClient = zenBpmClient;
        this.formsByElementId = new HashMap<>();
    }

    /**
     * Loads form schemas after all properties have been injected.
     * Called automatically by Spring via @PostConstruct.
     */
    @PostConstruct
    void loadForms() {
        String pattern = formsPath.endsWith("/") ? formsPath + "*.json" : formsPath + "/*.json";
        log.info("Loading form schemas from: {}", pattern);
        try {
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources(pattern);
            for (Resource resource : resources) {
                String filename = resource.getFilename();
                if (filename == null) continue;
                String elementId = filename.substring(0, filename.lastIndexOf('.'));
                String json = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
                formsByElementId.put(elementId, json);
                log.info("Loaded form schema for element '{}' ({} bytes)", elementId, json.length());
            }
        } catch (Exception e) {
            log.warn("Could not load form schemas from '{}': {}", pattern, e.getMessage());
        }
    }

    public List<TaskDto> getActiveTasks() {
        List<JobResponse> jobs = zenBpmClient.fetchActiveTasks();
        if (jobs.isEmpty()) {
            return Collections.emptyList();
        }

        // Collect unique processInstanceKeys and resolve processDefinitionKeys
        Map<String, String> instanceKeyToDefinitionKey = jobs.stream()
                .map(JobResponse::getProcessInstanceKey)
                .filter(k -> k != null)
                .distinct()
                .collect(Collectors.toMap(
                        instanceKey -> instanceKey,
                        instanceKey -> {
                            try {
                                ProcessInstanceResponse instance = zenBpmClient.fetchProcessInstance(instanceKey);
                                return instance != null ? instance.getProcessDefinitionKey() : null;
                            } catch (Exception e) {
                                log.warn("Could not fetch process instance {}: {}", instanceKey, e.getMessage());
                                return null;
                            }
                        }
                ));

        // Collect unique processDefinitionKeys and resolve BPMN element name maps
        Map<String, Map<String, String>> definitionKeyToNameMap = instanceKeyToDefinitionKey.values().stream()
                .filter(k -> k != null)
                .distinct()
                .collect(Collectors.toMap(
                        defKey -> defKey,
                        defKey -> {
                            try {
                                ProcessDefinitionResponse def = zenBpmClient.fetchProcessDefinition(defKey);
                                if (def != null && def.getBpmnData() != null) {
                                    return parseBpmnElementNames(def.getBpmnData());
                                }
                            } catch (Exception e) {
                                log.warn("Could not fetch process definition {}: {}", defKey, e.getMessage());
                            }
                            return Collections.emptyMap();
                        }
                ));

        return jobs.stream()
                .map(job -> {
                    String defKey = instanceKeyToDefinitionKey.get(job.getProcessInstanceKey());
                    Map<String, String> nameMap = defKey != null
                            ? definitionKeyToNameMap.getOrDefault(defKey, Collections.emptyMap())
                            : Collections.emptyMap();
                    String name = nameMap.get(job.getElementId());
                    return toDto(job, name);
                })
                .toList();
    }

    public void completeTask(String jobKey, Map<String, Object> variables) {
        zenBpmClient.completeJob(jobKey, variables);
    }

    /**
     * Parses BPMN XML and returns a map of flowElement id → name.
     */
    private Map<String, String> parseBpmnElementNames(String bpmnXml) {
        Map<String, String> result = new HashMap<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            Document doc = factory.newDocumentBuilder()
                    .parse(new InputSource(new StringReader(bpmnXml)));
            NodeList elements = doc.getElementsByTagName("*");
            for (int i = 0; i < elements.getLength(); i++) {
                org.w3c.dom.Element el = (org.w3c.dom.Element) elements.item(i);
                String id = el.getAttribute("id");
                String name = el.getAttribute("name");
                if (id != null && !id.isEmpty() && name != null && !name.isEmpty()) {
                    result.put(id, name);
                }
            }
        } catch (Exception e) {
            log.warn("Failed to parse BPMN XML for element names: {}", e.getMessage());
        }
        return result;
    }

    private TaskDto toDto(JobResponse job, String name) {
        Instant createdAt = job.getCreatedAt() != null
                ? Instant.parse(job.getCreatedAt())
                : null;

        // Merge ZEN_FORM into variables if a form schema exists for this element.
        Map<String, Object> variables = job.getVariables() != null
                ? new HashMap<>(job.getVariables())
                : new HashMap<>();
        String formJson = formsByElementId.get(job.getElementId());
        if (formJson != null && !variables.containsKey("ZEN_FORM")) {
            variables.put("ZEN_FORM", formJson);
        }

        return new TaskDto(
                job.getKey(),
                job.getElementId(),
                name,
                job.getProcessInstanceKey(),
                job.getAssignee(),
                createdAt,
                Collections.unmodifiableMap(variables)
        );
    }
}
