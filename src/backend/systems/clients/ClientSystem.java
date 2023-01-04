package src.backend.systems.clients;

import lombok.extern.slf4j.Slf4j;
import src.backend.infrastructure.ConnectionManager;
import src.backend.systems.clients.repositories.ClientRepository;
import src.backend.systems.clients.repositories.ClientRepositoryInterface;
import src.model.Client;
import src.model.exceptions.InvalidClientDataException;
import src.model.factories.ClientFactory;

import java.util.Optional;

@Slf4j
public class ClientSystem {
    private final ConnectionManager connectionManager;

    private final ClientRepositoryInterface clientRepo;

    public ClientSystem(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
        this.clientRepo = new ClientRepository(connectionManager);
    }

    public boolean addNewClient(String name, String secondName, String email, String phoneNumber) {
        if (clientRepo.clientEmailExists(email)) {
            log.error("Client with email - '{}' already exists!", email);
            return false;
        }

        try {
            var client = ClientFactory.createNewClient(name, secondName, email, phoneNumber);
            return clientRepo.addNewClient(client);
        } catch (InvalidClientDataException e) {
            log.error(e.getMessage());
            return false;
        }
    }

    public Optional<Client> getClient(int id) {
        return clientRepo.getClient(id);
    }

    public boolean changeClientEmail(int id, String email) {
        if(email == null || !email.contains("@")) {
            log.error("New email address - '{}' is invalid!", email);
            return false;
        }

        if(clientRepo.clientEmailExists(email)) {
            log.error("New email address - '{}' is already occupied!", email);
            return false;
        }

        clientRepo.changeEmail(id, email);
        return true;
    }

    public boolean changeClientPhoneNumber(int id, String phoneNumber) {
        if(phoneNumber == null) {
            log.error("New phone number is empty!");
            return false;
        }

        clientRepo.changePhoneNumber(id, phoneNumber);
        return true;
    }
}
