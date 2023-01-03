package src.model.client;

import lombok.Getter;

@Getter
public class InvalidClientDataException extends Exception {
    private final String message;
    private final String fieldName;

    public InvalidClientDataException(String fieldName) {
        this.fieldName = fieldName;
        this.message = String.format("Invalid field value for field: '%d'", fieldName);
    }
}
