package src.model.exceptions;

import lombok.Getter;

@Getter
public class InvalidClientDataException extends Exception {
    private final String message;
    private final String fieldName;

    public InvalidClientDataException(String fieldName) {
        this.fieldName = fieldName;
        this.message = String.format("Invalid value for field: '%s'", fieldName);
    }
}
