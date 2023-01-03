package src.backend.systems.repositories;

import src.backend.infrastructure.ConnectionManager;

import java.sql.SQLException;
import java.util.Optional;

public class ClientLoginRepository extends AbstractRepository implements ClientLoginRepositoryInterface {

    public ClientLoginRepository(ConnectionManager connectionManager) {
        super(connectionManager);
    }

    @Override
    public Optional<Integer> getClientIdFromEmailAddress(String emailAddress) {
        var query = String.format("SELECT id FROM klient WHERE email = '%s'", emailAddress);

        var result = executor.executeSelect(query);

        try {
            if(result.next()) {
                return Optional.of(result.getInt("id"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }
}
