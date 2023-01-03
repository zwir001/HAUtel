package src.backend.infrastructure.exceptions;

import lombok.Getter;

@Getter
public abstract class PostgresQueryException extends RuntimeException {
    String message;
    String query;
    String user;
}
