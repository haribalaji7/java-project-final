package com.bloodlink.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DonorNotAvailableException extends RuntimeException {
    public DonorNotAvailableException(String message) {
        super(message);
    }
}
