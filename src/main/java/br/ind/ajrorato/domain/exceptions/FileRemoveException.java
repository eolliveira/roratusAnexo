package br.ind.ajrorato.domain.exceptions;

public class FileRemoveException extends RuntimeException {

    public FileRemoveException(String message) {
        super(message);
    }

    public FileRemoveException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileRemoveException(Exception e) {
        super(e.getMessage(), e);
    }

}
