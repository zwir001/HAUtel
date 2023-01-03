package src.backend.systems;

import lombok.extern.slf4j.Slf4j;
import src.backend.authentification.UserAuthSystem;
import src.backend.infrastructure.ConnectionManager;
import src.backend.infrastructure.DatabaseUser;
import src.backend.systems.repositories.ClientLoginRepository;
import src.backend.systems.repositories.ClientLoginRepositoryInterface;
import src.model.client.Client;

import java.util.Optional;

@Slf4j
public class BaseSystem {
    private ConnectionManager connectionManager;

    private final UserAuthSystem userAuthSystem;
    private ClientSystem clientSystem;

    private final ClientLoginRepositoryInterface clientLoginRepo;

    private int userId;
    private boolean isEmployee;

    public BaseSystem() {
        this.userAuthSystem = new UserAuthSystem();
        this.clientLoginRepo = new ClientLoginRepository(DatabaseUser.CLIENT.getConnectionManager());
    }

    public boolean login(String login, char[] password, boolean isEmployee) {
        if (isEmployee) {
            var employeeId = Integer.parseInt(login);

            if (userAuthSystem.checkPassword(employeeId, password, true)) {
                userId = employeeId;
                this.isEmployee = true;
                connectionManager = DatabaseUser.EMPLOYEE.getConnectionManager();
                initializeSystems();

                log.info("User successfully logged as employee: '{}'", userId);
                return true;
            }
        }
        var clientId = clientLoginRepo.getClientIdFromEmailAddress(login);
        if (clientId.isPresent()) {
            if (userAuthSystem.checkPassword(clientId.get(), password, false)) {
                userId = clientId.get();
                this.isEmployee = false;
                connectionManager = DatabaseUser.CLIENT.getConnectionManager();
                initializeSystems();

                log.info("User successfully logged as client: '{}'", userId);
                return true;
            }
        }

        log.warn("Failed to login with arguments: login: '{}', password '{}', isEmployee '{}'",
                login,
                password,
                isEmployee ? "True" : "False"
        );
        return false;
    }

    public boolean addNewClient(String name, String secondName, String email, String phoneNumber, char[] password) {
        if (clientSystem.addNewClient(name, secondName, email, phoneNumber)) {
            log.error("New client not added! data: name - '{}', secondName - '{}', email - '{}', phoneNumber - '{}'",
                    name,
                    secondName,
                    email,
                    phoneNumber
            );
            return false;
        }

        var clientId = clientLoginRepo.getClientIdFromEmailAddress(email);
        if (clientId.isPresent() && userAuthSystem.addNewClientPassword(clientId.get(), password)) {
            log.error("Client with email - '{}' not found! Aborting to set password!", email);
            return false;
        }

        return true;
    }

    public Optional<Client> getClientData() {
        return clientSystem.getClient(userId);
    }

    public Optional<Client> getClientData(int id) {
        return clientSystem.getClient(id);
    }

    private void initializeSystems() {
        clientSystem = new ClientSystem(connectionManager, userAuthSystem);
    }
}
