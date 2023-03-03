package com.example.beliemeserver.controller.responsebody;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ExceptionResponse {
    private final String name;
    private final String message;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final List<ValidationError> errors;

    public ExceptionResponse(String name, String message) {
        this.name = name;
        this.message = message;
        this.errors = new ArrayList<>();
    }

    public ExceptionResponse(String name, String message, List<ValidationError> errors) {
        this.name = name;
        this.message = message;
        this.errors = errors;
    }

    @Getter
    public static class ValidationError {
        private final String field;
        private final String message;

        public ValidationError(String field, String message) {
            this.field = field;
            this.message = message;
        }

        public static ValidationError of(final FieldError fieldError) {
            return new ValidationError(
                    fieldError.getField(), fieldError.getDefaultMessage());
        }
    }
}
