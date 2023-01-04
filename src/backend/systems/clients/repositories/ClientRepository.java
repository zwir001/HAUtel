package src.backend.systems.clients.repositories;

import src.backend.infrastructure.ConnectionManager;
import src.backend.systems.repositories.AbstractRepository;
import src.model.Client;

import java.sql.SQLException;
import java.util.Optional;

public class ClientRepository extends AbstractRepository implements ClientRepositoryInterface {
    public ClientRepository(ConnectionManager connectionManager) {
        super(connectionManager);
    }

    @Override
    public Optional<Client> getClient(int id) {
        var query = String.format("SELECT * FROM klient WHERE id = %d", id);
        var result = executor.executeSelect(query);

        try {
            if (result.next()) {
                return Optional.of(Client.builder()
                        .id(result.getInt("id"))
                        .name(result.getString("imie"))
                        .secondName(result.getString("nazwisko"))
                        .phoneNumber(result.getString("numertelefonu"))
                        .charges(result.getFloat("naleznosci"))
                        .build()
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }

    @Override
    public boolean clientEmailExists(String email) {
        var query = String.format("SELECT id FROM klient WHERE email = '%s'", email);
        var result = executor.executeSelect(query);

        try {
            return result.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean addNewClient(Client client) {
        var query = String.format("INSERT INTO Klient (imie, nazwisko, email, numertelefonu)" +
                        "VALUES ('%s', '%s', '%s', '%s')",
                client.getName(),
                client.getSecondName(),
                client.getEmail(),
                client.getPhoneNumber()
        );

        return executor.executeQuery(query);
    }

    @Override
    public void changeEmail(int id, String email) {
        var query = String.format("UPDATE klient SET email = '%s' WHERE id = %d", email, id);
        executor.executeUpdate(query);
    }

    @Override
    public void changePhoneNumber(int id, String phoneNumber) {
        var query = String.format("UPDATE klient SET numertelefonu = '%s' WHERE id = %d", phoneNumber, id);
        executor.executeUpdate(query);
    }
}
