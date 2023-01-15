package src.backend.systems.authentification;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import src.backend.infrastructure.ConnectionManager;
import src.backend.infrastructure.DatabaseUser;
import src.backend.systems.authentification.repositories.UserAuthRepository;
import src.backend.systems.authentification.repositories.UserAuthRepositoryInterface;

public class UserAuthSystem {
    private static final String ENCRYPTION_SEED = "112358";
    private final StandardPBEStringEncryptor encryptor;

    private final ConnectionManager employeeAuthManager = DatabaseUser.EMPLOYEE.getConnectionManager();
    private final ConnectionManager clientAuthManager = DatabaseUser.CLIENT.getConnectionManager();

    private final UserAuthRepositoryInterface authRepo;

    public UserAuthSystem() {
        this.encryptor = new StandardPBEStringEncryptor();
        this.encryptor.setPassword(ENCRYPTION_SEED);

        this.authRepo = new UserAuthRepository(employeeAuthManager, clientAuthManager);
    }

    public boolean checkPassword(int id, char[] password, boolean isEmployee) {
        var passwordString = String.valueOf(password);
        if(isEmployee) {
            return validateEmployeePassword(id, passwordString);
        }

        return validateClientPassword(id, passwordString);
    }

    public void updateClientPassword(int id, char[] newPassword) {
        var passwordString = String.valueOf(newPassword);
        authRepo.updateClientPassword(id, encryptPassword(passwordString));
    }

    public boolean addNewClientPassword(int id, char[] password) {
        var passwordString = String.valueOf(password);
        return authRepo.addNewClientPassword(id, encryptPassword(passwordString));
    }

    private boolean validateEmployeePassword(int id, String password) {
        var encryptedPassword = authRepo.getEmployeePassword(id);
        if(encryptedPassword.isEmpty()) {
            return false;
        }

        return password.equals(decryptPassword(encryptedPassword.get()));
    }

    private boolean validateClientPassword(int id, String password) {
        var encryptedPassword = authRepo.getClientPassword(id);
        if(encryptedPassword.isEmpty()) {
            return false;
        }

        return password.equals(decryptPassword(encryptedPassword.get()));
    }

    private String encryptPassword(String password) {
        return encryptor.encrypt(password);
    }

    private String decryptPassword(String password) {
        return encryptor.decrypt(password);
    }
}
