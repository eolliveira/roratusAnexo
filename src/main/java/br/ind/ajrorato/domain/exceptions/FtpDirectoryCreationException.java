package br.ind.ajrorato.domain.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class FtpDirectoryCreationException extends RuntimeException {
    private final HttpStatus httpStatus;

    public FtpDirectoryCreationException(String message, Throwable cause) {
        super(message, cause);
        this.httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
    }
}
