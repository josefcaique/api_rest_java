package com.josef.api_rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RequiredObjectIsNullException extends RuntimeException {

    public RequiredObjectIsNullException() {

        super("it is not allowed to persist a null object");
    }

    public RequiredObjectIsNullException(String message) {
        super(message);
    }
}
