package eu.bpm4.portal.task;

import eu.bpm4.portal.task.model.CompleteTaskRequest;
import eu.bpm4.portal.task.model.TaskDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // TODO: add @PreAuthorize annotations here when Spring Security is enabled
    @GetMapping
    public List<TaskDto> listTasks() {
        return taskService.getActiveTasks();
    }

    // TODO: add @PreAuthorize annotations here when Spring Security is enabled
    @PostMapping("/{key}/complete")
    public ResponseEntity<Void> completeTask(@PathVariable String key,
                                             @RequestBody CompleteTaskRequest request) {
        taskService.completeTask(key, request.variables());
        return ResponseEntity.noContent().build();
    }
}
