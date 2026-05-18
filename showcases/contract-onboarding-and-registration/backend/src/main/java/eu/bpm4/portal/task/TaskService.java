package eu.bpm4.portal.task;

import eu.bpm4.portal.task.model.TaskDto;
import eu.bpm4.portal.zenbpm.ZenBpmClient;
import eu.bpm4.portal.zenbpm.model.JobResponse;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Service
public class TaskService {

    private final ZenBpmClient zenBpmClient;

    public TaskService(ZenBpmClient zenBpmClient) {
        this.zenBpmClient = zenBpmClient;
    }

    public List<TaskDto> getActiveTasks() {
        return zenBpmClient.fetchActiveTasks().stream()
                .map(this::toDto)
                .toList();
    }

    public void completeTask(String jobKey, Map<String, Object> variables) {
        zenBpmClient.completeJob(jobKey, variables);
    }

    private TaskDto toDto(JobResponse job) {
        Instant createdAt = job.getCreatedAt() != null
                ? Instant.ofEpochMilli(job.getCreatedAt())
                : null;
        return new TaskDto(
                job.getKey(),
                job.getElementId(),
                job.getProcessInstanceKey(),
                job.getAssignee(),
                createdAt,
                job.getVariables()
        );
    }
}
