package br.ind.ajrorato.http.exceptions;

import br.ind.ajrorato.domain.exceptions.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.time.Instant;

@RestControllerAdvice
public class ResourceExceptionHandler {

    @Value("${spring.servlet.multipart.max-file-size}")
    private String maxUploadSize;

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<StandardError> handleBusinessException(IllegalArgumentException ex, HttpServletRequest request) {
        var httpStatus = HttpStatus.BAD_REQUEST;
        return ResponseEntity
                .status(httpStatus.value())
                .body(StandardError.builder()
                        .timestamp(Instant.now())
                        .status(httpStatus.value())
                        .error(httpStatus.getReasonPhrase())
                        .message(ex.getMessage())
                        .path(request.getRequestURI())
                        .build());
    }

    @ExceptionHandler(FtpDirectoryCreationException.class)
    public ResponseEntity<StandardError> handleBusinessException(FtpDirectoryCreationException ex, HttpServletRequest request) {
        var httpStatus = ex.getHttpStatus();
        return ResponseEntity
                .status(httpStatus.value())
                .body(StandardError.builder()
                        .timestamp(Instant.now())
                        .status(httpStatus.value())
                        .error(httpStatus.getReasonPhrase())
                        .message(ex.getMessage())
                        .path(request.getRequestURI())
                        .build());
    }

    @ExceptionHandler(FileUploadException.class)
    public ResponseEntity<StandardError> handleBusinessException(FileUploadException ex, HttpServletRequest request) {
        var httpStatus = ex.getHttpStatus();
        return ResponseEntity
                .status(httpStatus.value())
                .body(StandardError.builder()
                        .timestamp(Instant.now())
                        .status(httpStatus.value())
                        .error(httpStatus.getReasonPhrase())
                        .message(ex.getMessage())
                        .path(request.getRequestURI())
                        .build());
    }

    @ExceptionHandler(FileDownloadException.class)
    public ResponseEntity<StandardError> handleBusinessException(FileDownloadException ex, HttpServletRequest request) {
        var httpStatus = ex.getHttpStatus();
        return ResponseEntity
                .status(httpStatus.value())
                .body(StandardError.builder()
                        .timestamp(Instant.now())
                        .status(httpStatus.value())
                        .error(httpStatus.getReasonPhrase())
                        .message(ex.getMessage())
                        .path(request.getRequestURI())
                        .build());
    }

    @ExceptionHandler(FileRemoveException.class)
    public ResponseEntity<StandardError> handleBusinessException(FileRemoveException ex, HttpServletRequest request) {
        var httpStatus = ex.getHttpStatus();
        return ResponseEntity
                .status(httpStatus.value())
                .body(StandardError.builder()
                        .timestamp(Instant.now())
                        .status(httpStatus.value())
                        .error(httpStatus.getReasonPhrase())
                        .message(ex.getMessage())
                        .path(request.getRequestURI())
                        .build());
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<StandardError> handleBusinessException(MaxUploadSizeExceededException ex, HttpServletRequest request) {
        var httpStatus = HttpStatus.PAYLOAD_TOO_LARGE;
        return ResponseEntity
                .status(httpStatus.value())
                .body(StandardError.builder()
                        .timestamp(Instant.now())
                        .status(httpStatus.value())
                        .error(httpStatus.getReasonPhrase())
                        .message("O tamanho do arquivo excede o limite permitido de " + maxUploadSize)
                        .path(request.getRequestURI())
                        .build());
    }

    @ExceptionHandler(FileCompressionException.class)
    public ResponseEntity<StandardError> handleBusinessException(FileCompressionException ex, HttpServletRequest request) {
        var httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
        return ResponseEntity
                .status(httpStatus.value())
                .body(StandardError.builder()
                        .timestamp(Instant.now())
                        .status(httpStatus.value())
                        .error(httpStatus.getReasonPhrase())
                        .message(ex.getMessage())
                        .path(request.getRequestURI())
                        .build());
    }
}
