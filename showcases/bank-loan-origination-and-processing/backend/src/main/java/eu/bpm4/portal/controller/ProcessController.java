package eu.bpm4.portal.controller;

import eu.bpm4.portal.model.StartProcessRequest;
import eu.bpm4.portal.service.ProcessService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
