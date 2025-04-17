package br.ind.ajrorato.domain.exceptions;

public class FileUploadException extends RuntimeException {

    public FileUploadException(String message) {
        super(message);
    }

    public FileUploadException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileUploadException(Exception e) {
        super(e.getMessage(), e);
    }

}
