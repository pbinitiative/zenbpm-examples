package eu.bpm4.portal.task;

import eu.bpm4.portal.task.model.TaskDto;
import eu.bpm4.portal.zenbpm.ZenBpmClient;
import eu.bpm4.portal.zenbpm.model.JobResponse;
import eu.bpm4.portal.zenbpm.model.ProcessDefinitionResponse;
import eu.bpm4.portal.zenbpm.model.ProcessInstanceResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
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

    public TaskService(ZenBpmClient zenBpmClient) {
        this.zenBpmClient = zenBpmClient;
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
        return new TaskDto(
                job.getKey(),
                job.getElementId(),
                name,
                job.getProcessInstanceKey(),
                job.getAssignee(),
                createdAt,
                job.getVariables()
        );
    }
}
