package de.hskl.itanalyst.alwi;

import de.hskl.itanalyst.alwi.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(WayNotComputableException.class)
    public ResponseEntity<ApiError> handleNotComputable(WayNotComputableException ex) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "Try other addresses." , ex.getMessage());
        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }

    @ExceptionHandler(WayNotFoundException.class)
    public ResponseEntity<ApiError> handleWayNotFound(WayNotFoundException ex) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, "Use a known way." , ex.getMessage());
        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }

    @ExceptionHandler(NodeNotFoundException.class)
    public ResponseEntity<ApiError> handleNodeNotFound(NodeNotFoundException ex) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, "Use a known node." , ex.getMessage());
        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }

    @ExceptionHandler(StreetNotFoundException.class)
    public ResponseEntity<ApiError> handleStreetNotFound(StreetNotFoundException ex) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, "Use a known street." , ex.getMessage());
        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }
}