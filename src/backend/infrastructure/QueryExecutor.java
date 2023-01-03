package src.backend.infrastructure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import src.backend.infrastructure.exceptions.PostgresDataModificationQueryException;
import src.backend.infrastructure.exceptions.PostgresSelectQueryException;

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

    public boolean executeQuery(String query) {
        try {
            var result = connectionManager.connect().createStatement().executeQuery(query);
            if(result.next()) {
                return true;
            }
        } catch (SQLException e) {
            throw new PostgresDataModificationQueryException(e.getMessage(), query, connectionManager.getUser());
        }

        return false;
    }

    public void executeUpdate(String query) {
        try {
            connectionManager.connect().createStatement().executeUpdate(query);
        } catch (SQLException e) {
            throw new PostgresDataModificationQueryException(e.getMessage(), query, connectionManager.getUser());
        }
    }
}
