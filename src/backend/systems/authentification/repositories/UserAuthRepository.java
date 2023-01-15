package src.backend.systems.authentification.repositories;

import src.backend.infrastructure.ConnectionManager;
import src.backend.infrastructure.QueryExecutor;

import java.sql.SQLException;
import java.util.Optional;

public class UserAuthRepository implements UserAuthRepositoryInterface {
    private final QueryExecutor employeeQueryExecutor;
    private final QueryExecutor clientQueryExecutor;

    public UserAuthRepository(ConnectionManager employeeAuthManager, ConnectionManager clientAuthManager) {
        this.employeeQueryExecutor = new QueryExecutor(employeeAuthManager);
        this.clientQueryExecutor = new QueryExecutor(clientAuthManager);
    }

    @Override
    public Optional<String> getEmployeePassword(int id) {
        var query = String.format("SELECT haslo FROM pracownik WHERE id = %d", id);

        var result = employeeQueryExecutor.executeSelect(query);

        try {
            if(result.next()) {
                return Optional.of(result.getString("haslo"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }

    @Override
    public Optional<String> getClientPassword(int id) {
        var query = String.format("SELECT haslo FROM haslo WHERE klientid = %d", id);

        var result = clientQueryExecutor.executeSelect(query);

        try {
            if(result.next()) {
                return Optional.of(result.getString("haslo"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }

    @Override
    public void updateClientPassword(int id, String newPassword) {
        var query = String.format("UPDATE haslo SET haslo = '%s' WHERE klientid = %d", newPassword, id);
        clientQueryExecutor.executeUpdate(query);
    }

    @Override
    public boolean addNewClientPassword(int id, String password) {
        var query = String.format("INSERT INTO haslo (klientid, haslo) values ('%d', '%s')", id, password);

        return clientQueryExecutor.executeQuery(query);
    }
}
