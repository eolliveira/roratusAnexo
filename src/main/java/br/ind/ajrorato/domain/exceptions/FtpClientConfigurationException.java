package br.ind.ajrorato.domain.exceptions;

public class FtpClientConfigurationException extends RuntimeException {
    public FtpClientConfigurationException(String message) {
        super(message);
    }

    public FtpClientConfigurationException(Exception e) {
        super(e.getMessage(), e);
    }
}
