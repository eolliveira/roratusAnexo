package br.ind.ajrorato.domain.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class FileUploadException extends RuntimeException {
    private final HttpStatus httpStatus;

    public FileUploadException(String message) {
        super(message);
        this.httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
    }
}
