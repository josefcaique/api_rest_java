package com.josef.api_rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class FileNotSupportedException extends RuntimeException {

    public FileNotSupportedException(String message) {
        super(message);
    }
}
