package com.devjoliveira.jocommerce.dto;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ValidationError extends CustomError {

    private List<FieldMessage> erros = new ArrayList<>();

    public ValidationError(Instant timestamp, Integer status, String error, String path) {
        super(timestamp, status, error, path);
    }

    public void addError(String fieldName, String message) {

        erros.removeIf(x -> x.fieldName().equals(fieldName));
        erros.add(new FieldMessage(fieldName, message));

    }

    public List<FieldMessage> getFieldMessages() {
        return erros;
    }

}
