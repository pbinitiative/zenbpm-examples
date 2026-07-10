package eu.bpm4.portal.exception;

import eu.bpm4.portal.zenbpm.ZenBpmException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ZenBpmException.class)
    public ResponseEntity<Map<String, Object>> handleZenBpmException(ZenBpmException ex) {
        int statusCode = ex.getStatus().value();
        HttpStatus responseStatus = statusCode >= 500
                ? HttpStatus.BAD_GATEWAY
                : HttpStatus.valueOf(statusCode);

        return ResponseEntity.status(responseStatus).body(errorBody(
                responseStatus.value(),
                responseStatus.getReasonPhrase(),
                ex.getMessage()
        ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .reduce((a, b) -> a + "; " + b)
                .orElse("Validation failed");
        return ResponseEntity.badRequest().body(errorBody(400, "Bad Request", message));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
        log.error("Unhandled exception", ex);
        return ResponseEntity.internalServerError().body(errorBody(
                500, "Internal Server Error", "An unexpected error occurred."
        ));
    }

    private Map<String, Object> errorBody(int status, String error, String message) {
        return Map.of("status", status, "error", error, "message", message != null ? message : "");
    }
}
