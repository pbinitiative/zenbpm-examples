package eu.bpm4.portal.zenbpm;

import org.springframework.http.HttpStatusCode;

public class ZenBpmException extends RuntimeException {

    private final HttpStatusCode status;

    public ZenBpmException(HttpStatusCode status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatusCode getStatus() {
        return status;
    }
}
