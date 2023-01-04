package src.backend.systems.clients.repositories;

import src.model.Client;

import java.util.Optional;

public interface ClientRepositoryInterface {
    Optional<Client> getClient(int id);

    boolean clientEmailExists(String email);

    boolean addNewClient(Client client);

    void changeEmail(int id, String email);

    void changePhoneNumber(int id, String phoneNumber);
}
