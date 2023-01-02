package src.databaseConnection.exceptions;

import lombok.Getter;

@Getter
public abstract class PostgresQueryException extends RuntimeException {
    String message;
    String query;
    String user;
}
