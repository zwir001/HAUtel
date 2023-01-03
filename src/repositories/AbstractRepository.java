package src.repositories;

import src.infrastructure.ConnectionManager;
import src.infrastructure.QueryExecutor;

public class AbstractRepository {
    protected QueryExecutor executor;

    protected AbstractRepository(ConnectionManager connectionManager) {
        this.executor = new QueryExecutor(connectionManager);
    }
}
