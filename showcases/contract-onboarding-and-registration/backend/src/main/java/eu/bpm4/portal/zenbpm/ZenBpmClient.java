package eu.bpm4.portal.zenbpm;

import eu.bpm4.portal.zenbpm.model.CompleteJobRequest;
import eu.bpm4.portal.zenbpm.model.JobResponse;
import eu.bpm4.portal.zenbpm.model.PartitionedResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class ZenBpmClient {

    private final WebClient webClient;

    public ZenBpmClient(WebClient zenBpmWebClient) {
        this.webClient = zenBpmWebClient;
    }

    public List<JobResponse> fetchActiveTasks() {
        PartitionedResponse<JobResponse> response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/jobs")
                        .queryParam("state", "active")
                        .queryParam("jobType", "user-task")
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse ->
                        clientResponse.bodyToMono(String.class)
                                .flatMap(body -> Mono.error(
                                        new ZenBpmException(clientResponse.statusCode(),
                                                "ZenBPM returned: HTTP " + clientResponse.statusCode().value() + " — " + body))))
                .bodyToMono(new ParameterizedTypeReference<PartitionedResponse<JobResponse>>() {})
                .block();

        if (response == null || response.getPartitions() == null) {
            return Collections.emptyList();
        }

        return response.getPartitions().stream()
                .filter(p -> p.getItems() != null)
                .flatMap(p -> p.getItems().stream())
                .toList();
    }

    public void completeJob(String jobKey, Map<String, Object> variables) {
        webClient.post()
                .uri("/jobs/{jobKey}/complete", jobKey)
                .bodyValue(new CompleteJobRequest(variables))
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse ->
                        clientResponse.bodyToMono(String.class)
                                .flatMap(body -> Mono.error(
                                        new ZenBpmException(clientResponse.statusCode(),
                                                "ZenBPM returned: HTTP " + clientResponse.statusCode().value() + " — " + body))))
                .bodyToMono(Void.class)
                .block();
    }

    public void startProcessInstance(String processKey, Map<String, Object> variables, String businessKey) {
        Map<String, Object> body = new java.util.HashMap<>();
        body.put("variables", variables);
        if (businessKey != null) {
            body.put("businessKey", businessKey);
        }

        webClient.post()
                .uri("/processes/{processKey}/instances", processKey)
                .bodyValue(body)
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse ->
                        clientResponse.bodyToMono(String.class)
                                .flatMap(body2 -> Mono.error(
                                        new ZenBpmException(clientResponse.statusCode(),
                                                "ZenBPM returned: HTTP " + clientResponse.statusCode().value() + " — " + body2))))
                .bodyToMono(Void.class)
                .block();
    }
}
