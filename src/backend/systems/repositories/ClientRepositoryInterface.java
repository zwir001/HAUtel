package src.backend.systems.repositories;

import src.model.Client;

import java.util.Optional;

public interface ClientRepositoryInterface {
    Optional<Client> getClient(int id);

    boolean clientEmailExists(String email);

    boolean addNewClient(Client client);
}
