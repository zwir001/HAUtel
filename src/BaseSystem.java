package src;

import lombok.extern.slf4j.Slf4j;
import src.authentification.UserAuthSystem;
import src.infrastructure.ConnectionManager;
import src.infrastructure.DatabaseUser;
import src.repositories.ClientLoginRepository;
import src.repositories.ClientLoginRepositoryInterface;

@Slf4j
public class BaseSystem {
    private ConnectionManager connectionManager;

    private final UserAuthSystem userAuthSystem;

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

                log.info("User successfully logged as employee: '{}'", userId);
                return true;
            }
        }
        var clientId = clientLoginRepo.getClientIdFromEmailAddress(login);
        if (clientId.isPresent()) {
            if (userAuthSystem.checkPassword(clientId.get(), password, false)) {
                userId = clientId.get();
                this.isEmployee = false;
                connectionManager = DatabaseUser.EMPLOYEE.getConnectionManager();

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
}
