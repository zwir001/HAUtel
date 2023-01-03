package src.backend.systems.repositories;

import src.backend.infrastructure.ConnectionManager;
import src.backend.infrastructure.QueryExecutor;

public class AbstractRepository {
    protected QueryExecutor executor;

    protected AbstractRepository(ConnectionManager connectionManager) {
        this.executor = new QueryExecutor(connectionManager);
    }
}
