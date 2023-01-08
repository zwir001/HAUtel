package src.backend.systems;

import lombok.extern.slf4j.Slf4j;
import src.backend.infrastructure.ConnectionManager;
import src.backend.infrastructure.DatabaseUser;
import src.backend.systems.authentification.UserAuthSystem;
import src.backend.systems.clients.ClientSystem;
import src.backend.systems.pets.PetSystem;
import src.backend.systems.repositories.ClientLoginRepository;
import src.backend.systems.repositories.ClientLoginRepositoryInterface;
import src.backend.systems.reservations.RequestedServiceStatus;
import src.backend.systems.reservations.ReservationSystem;
import src.model.Client;
import src.model.Pet;
import src.model.ReservationClientView;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class BaseSystem {
    private ConnectionManager connectionManager;

    private final UserAuthSystem userAuthSystem;
    private ClientSystem clientSystem;
    private PetSystem petSystem;
    private ReservationSystem reservationSystem;

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

    public boolean addNewClient(String name, String secondName, String email, String phoneNumber, char[] password,
                                String petName, int speciesId, String drugs, String allergy) {
        if (!new ClientSystem().addNewClient(name, secondName, email, phoneNumber)) {
            log.error("New client not added! data: name - '{}', secondName - '{}', email - '{}', phoneNumber - '{}'",
                    name,
                    secondName,
                    email,
                    phoneNumber
            );
            return false;
        }

        var clientId = clientLoginRepo.getClientIdFromEmailAddress(email);
        if (clientId.isEmpty() || !userAuthSystem.addNewClientPassword(clientId.get(), password)) {
            log.error("Client with email - '{}' not found! Aborting to set password!", email);
            return false;
        }

        if(!new PetSystem().addNewPet(clientId.get(), petName, speciesId, drugs, allergy)) {
            log.error("Failed to add first pet to client: '{}'!", clientId.get());
            return false;
        }

        return true;
    }

    public Optional<Client> getClientData() {
        return clientSystem.getClient(userId);
    }

    public Optional<Client> getClientData(int id) {
        if(!isEmployee) {
            return Optional.empty();
        }
        return clientSystem.getClient(id);
    }

    public void changeClientPassword(char[] newPassword) {
        userAuthSystem.updateClientPassword(userId, newPassword);
    }

    public boolean changeClientEmail(String newEmail) {
        return clientSystem.changeClientEmail(userId, newEmail);
    }

    public boolean changeClientPhoneNumber(String newPhoneNumber) {
        return clientSystem.changeClientPhoneNumber(userId, newPhoneNumber);
    }

    public boolean addNewPet(String petName, int speciesId, String drugs, String allergy) {
        return petSystem.addNewPet(userId, petName, speciesId, drugs, allergy);
    }

    public Collection<Pet> getClientPets(int clientId) {
        return petSystem.getAllClientPets(clientId);
    }

    public boolean addNewReservation(int petId, String date, int duration) {
        return reservationSystem.makeNewReservation(userId, petId, date, duration);
    }

    public Map<Integer, Collection<ReservationClientView>> getClientReservations(int clientId) {
        return reservationSystem.getClientReservations(clientId);
    }

    public Map<Integer, Collection<ReservationClientView>> getClientReservations() {
        return reservationSystem.getClientReservations(userId);
    }

    public boolean cancelReservation(int reservationId) {
        if(!isEmployee) {
            return false;
        }

        return reservationSystem.cancelReservation(reservationId);
    }

    public boolean completeReservation(int reservationId) {
        if(!isEmployee) {
            return false;
        }

        return reservationSystem.completeReservation(reservationId);
    }

    public Map<Integer, RequestedServiceStatus> orderAdditionalServices(int reservationId, Collection<Integer> requestedServicesIds) {
        return reservationSystem.addAdditionalServices(userId, reservationId, requestedServicesIds);
    }

    private void initializeSystems() {
        clientSystem = new ClientSystem(connectionManager);
        petSystem = new PetSystem(connectionManager);
        reservationSystem = new ReservationSystem(connectionManager, clientSystem, petSystem);
    }
}
