package src.backend.systems.authentification.repository;

import java.util.Optional;

public interface UserAuthRepositoryInterface {

    Optional<String> getEmployeePassword(int id);

    Optional<String> getClientPassword(int id);

    void updateClientPassword(int id, String newPassword);

    boolean addNewClientPassword(int id, String password);
}
