package br.ind.ajrorato.domain.exceptions;

public class DirectoryNotFoundException extends RuntimeException {

    public DirectoryNotFoundException(String message) {
        super(message);
    }

    public DirectoryNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public DirectoryNotFoundException(Exception e) {
        super(e.getMessage(), e);
    }

}
