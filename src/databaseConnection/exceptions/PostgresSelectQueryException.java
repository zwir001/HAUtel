package src.databaseConnection.exceptions;

public class PostgresSelectQueryException extends PostgresQueryException {
    public PostgresSelectQueryException(String message, String query, String user) {
        this.message = String.format("Exception while executing SELECT query: '%s', with user: '%s', original error: '%s'",
                query,
                user,
                message
        );
        this.query = query;
        this.user = user;
    }
}
