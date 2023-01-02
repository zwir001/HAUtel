package src.databaseConnection;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import src.databaseConnection.exceptions.PostgresDataModificationQueryException;
import src.databaseConnection.exceptions.PostgresSelectQueryException;

import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
@RequiredArgsConstructor
public class QueryExecutor {
    private final ConnectionManager connectionManager;

    public ResultSet executeSelect(String query) {
        try {
            return connectionManager.connect().createStatement().executeQuery(query);
        } catch (SQLException e) {
            throw new PostgresSelectQueryException(e.getMessage(), query, connectionManager.getUser());
        }
    }

    public void executeQuery(String query) {
        try {
            connectionManager.connect().createStatement().executeQuery(query);
        } catch (SQLException e) {
            throw new PostgresDataModificationQueryException(e.getMessage(), query, connectionManager.getUser());
        }
    }
}
