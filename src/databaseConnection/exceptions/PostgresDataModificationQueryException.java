package src.databaseConnection.exceptions;

public class PostgresDataModificationQueryException extends PostgresQueryException {
    public PostgresDataModificationQueryException(String message, String query, String user) {
        this.message = String.format("Exception while executing query: '%s', with user: '%s', original error: '%s'",
                query,
                user,
                message
        );
        this.query = query;
        this.user = user;
    }
}
