package eu.bpm4.portal.service;

import eu.bpm4.portal.client.ZenBpmClient;
import eu.bpm4.portal.config.PortalProperties;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ProcessService {

    private final ZenBpmClient zenBpmClient;
    private final PortalProperties portalProperties;

    public ProcessService(ZenBpmClient zenBpmClient, PortalProperties portalProperties) {
        this.zenBpmClient = zenBpmClient;
        this.portalProperties = portalProperties;
    }

    public void startProcess(Map<String, Object> variables, String businessKey) {
        String processKey = portalProperties.getProcess().getDefaultKey();
        zenBpmClient.startProcessInstance(processKey, variables, businessKey);
    }
}
