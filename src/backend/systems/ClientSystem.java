package src.backend.systems;

import lombok.extern.slf4j.Slf4j;
import src.backend.authentification.UserAuthSystem;
import src.backend.infrastructure.ConnectionManager;
import src.backend.systems.repositories.ClientRepository;
import src.backend.systems.repositories.ClientRepositoryInterface;
import src.model.client.Client;
import src.model.client.ClientFactory;
import src.model.client.InvalidClientDataException;

import java.util.Optional;

@Slf4j
public class ClientSystem {
    private final ConnectionManager connectionManager;
    private final ClientRepositoryInterface clientRepo;
    private final UserAuthSystem userAuthSystem;

    public ClientSystem(ConnectionManager connectionManager, UserAuthSystem userAuthSystem) {
        this.connectionManager = connectionManager;
        this.userAuthSystem = userAuthSystem;
        this.clientRepo = new ClientRepository(connectionManager);
    }

    public boolean addNewClient(String name, String secondName, String email, String phoneNumber) {
        if(clientRepo.clientEmailExists(email)) {
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
}
