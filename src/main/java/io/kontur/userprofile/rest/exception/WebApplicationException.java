package io.kontur.userprofile.rest.exception;

import org.springframework.http.HttpStatus;

public class WebApplicationException extends RuntimeException {

    public final String message;
    private final HttpStatus status;

    public WebApplicationException(String message, HttpStatus status) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
