package br.ind.ajrorato.domain.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class FtpClientConfigurationException extends RuntimeException {
    private final HttpStatus httpStatus;

    public FtpClientConfigurationException(String message) {
        super(message);
        this.httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
    }

    public FtpClientConfigurationException(Exception e) {
        super(e.getMessage(), e);
        this.httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
    }
}
