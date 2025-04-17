package br.ind.ajrorato.domain.exceptions;

public class FileNotFoundException extends RuntimeException {

    public FileNotFoundException(String message) {
        super(message);
    }

    public FileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileNotFoundException(Exception e) {
        super(e.getMessage(), e);
    }

}
