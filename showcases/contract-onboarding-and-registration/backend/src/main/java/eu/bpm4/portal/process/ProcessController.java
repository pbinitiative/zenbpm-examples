package eu.bpm4.portal.process;

import eu.bpm4.portal.process.model.StartProcessRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/processes")
public class ProcessController {

    private final ProcessService processService;

    public ProcessController(ProcessService processService) {
        this.processService = processService;
    }

    // TODO: add @PreAuthorize annotations here when Spring Security is enabled
    @PostMapping("/start")
    public ResponseEntity<Void> startProcess(@RequestBody StartProcessRequest request) {
        processService.startProcess(request.variables(), request.businessKey());
        return ResponseEntity.status(201).build();
    }
}
