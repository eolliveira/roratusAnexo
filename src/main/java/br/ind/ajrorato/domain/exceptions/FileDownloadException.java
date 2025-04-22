package br.ind.ajrorato.domain.exceptions;

public class FileDownloadException extends RuntimeException {

    public FileDownloadException(String message) {
        super(message);
    }

    public FileDownloadException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileDownloadException(Exception e) {
        super(e.getMessage(), e);
    }

}
