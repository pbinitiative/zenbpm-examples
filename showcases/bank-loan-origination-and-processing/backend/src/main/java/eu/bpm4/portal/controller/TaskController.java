package eu.bpm4.portal.controller;

import eu.bpm4.portal.model.CompleteTaskRequest;
import eu.bpm4.portal.model.TaskDto;
import eu.bpm4.portal.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
