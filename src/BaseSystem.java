package src;

import src.authentification.UserAuthSystem;
import src.infrastructure.ConnectionManager;
import src.infrastructure.DatabaseUser;
import src.repositories.ClientLoginRepository;
import src.repositories.ClientLoginRepositoryInterface;

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

                return true;
            }
        }

        var clientId = clientLoginRepo.getClientIdFromEmailAddress(login);
        if (clientId.isPresent()) {
            if (userAuthSystem.checkPassword(clientId.get(), password, false)) {
                userId = clientId.get();
                this.isEmployee = false;
                connectionManager = DatabaseUser.EMPLOYEE.getConnectionManager();

                return true;
            }
        }

        return false;
    }
}
