package src.backend.systems.reservations;

import lombok.extern.slf4j.Slf4j;
import src.backend.infrastructure.ConnectionManager;
import src.backend.systems.clients.ClientSystem;
import src.backend.systems.pets.PetSystem;
import src.backend.systems.reservations.repositories.*;
import src.model.Reservation;
import src.model.ReservationClientView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
public class ReservationSystem {
    private final ConnectionManager connectionManager;

    private final ClientSystem clientSystem;
    private final PetSystem petSystem;

    private final ReservationRepositoryInterface reservationRepo;
    private final OrderedServiceRepositoryInterface orderedServiceRepo;
    private final AdditionalServiceRepositoryInterface additionalServiceRepo;
    private final ReservationStatusRepositoryInterface reservationStatusRepo;


    public ReservationSystem(ConnectionManager connectionManager, ClientSystem clientSystem, PetSystem petSystem) {
        this.connectionManager = connectionManager;
        this.clientSystem = clientSystem;
        this.petSystem = petSystem;
        this.reservationRepo = new ReservationRepository(connectionManager);
        this.orderedServiceRepo = new OrderedServiceRepository(connectionManager);
        this.additionalServiceRepo = new AdditionalServiceRepository(connectionManager);
        this.reservationStatusRepo = new ReservationStatusRepository(connectionManager);
    }

    public boolean makeNewReservation(int clientId, int petId, String date, int duration) {
        var petOptional = petSystem.getPet(petId);

        if (petOptional.isEmpty() || petOptional.get().getOwnerId() != clientId) {
            log.error("Client - '{}' doesn't have pet with id - '{}'. Reservation not added!", clientId, petId);
            return false;
        }

        var pet = petOptional.get();

        if (!validateReservationDates(pet.getSpeciesId(), date, duration)) {
            log.error("Reservation for pet with id - '{}' is not possible in specified dates: date - '{}', duration - '{}' days",
                    petId,
                    date,
                    duration
            );
            return false;
        }

        var dailyCost = petSystem.getSpeciesDailyCost(pet.getSpeciesId());
        var reservationValue = dailyCost * duration;

        if (reservationRepo.addNewReservation(Reservation.builder()
                .animalId(petId)
                .startDate(date)
                .duration(duration)
                .value(reservationValue)
                .statusId(reservationStatusRepo.getStatusId(ReservationStatus.ACTIVE.getName()))
                .build())
        ) {
            updateClientCharges(clientId);
            return true;
        }

        return false;
    }

    public boolean cancelReservation(int reservationId) {
        if(!reservationRepo.reservationExists(reservationId)) {
            log.warn("Reservation - '{}' doesn't exist. Aborting!", reservationId);
            return false;
        }
        var cancelledStatusId = reservationStatusRepo.getStatusId(ReservationStatus.CANCELLED.getName());
        reservationRepo.changeReservationStatus(reservationId, cancelledStatusId);

        var clientId = reservationRepo.getClientIdFromReservation(reservationId);
        updateClientCharges(clientId);

        return orderedServiceRepo.deleteOrderedServicesForReservation(reservationId);
    }

    public boolean completeReservation(int reservationId) {
        if(!reservationRepo.reservationExists(reservationId)) {
            log.warn("Reservation - '{}' doesn't exist. Aborting!", reservationId);
            return false;
        }
        var completedStatusId = reservationStatusRepo.getStatusId(ReservationStatus.COMPLETED.getName());
        reservationRepo.changeReservationStatus(reservationId, completedStatusId);

        var clientId = reservationRepo.getClientIdFromReservation(reservationId);
        updateClientCharges(clientId);

        return true;
    }

    public Map<Integer, Collection<ReservationClientView>> getClientReservations(int clientId) {
        var clientPetIds = petSystem.getAllClientPetIds(clientId);

        var reservations = new HashMap<Integer, Collection<ReservationClientView>>();
        for (var petId : clientPetIds) {
            reservations.put(clientId, reservationRepo.getReservationsForPet(petId));
        }

        return reservations;
    }

    public Map<Integer, RequestedServiceStatus> addAdditionalServices(
            int clientId, int reservationId, Collection<Integer> requestedServicesIds) {
        if (clientId != reservationRepo.getClientIdFromReservation(reservationId)) {
            log.error("Reservation - '{}', not registered for client - '{}'!", reservationId, clientId);
        }

        var reservationOpt = reservationRepo.getReservation(reservationId);
        if (reservationOpt.isEmpty()) {
            log.error("Reservation with id - '{}' not found!", reservationId);
            return Collections.emptyMap();
        }

        var reservation = reservationOpt.get();

        if (reservation.getStatusId() != 1) {
            log.error("Aborting, can't order new services for not active reservation with id - '{}'!", reservationId);
            return Collections.emptyMap();
        }
        var alreadyOrderedServices = orderedServiceRepo.getOrderedServices(reservationId);

        var results = new HashMap<Integer, RequestedServiceStatus>();
        var newServicesTotalCost = 0f;
        for (var serviceId : requestedServicesIds) {
            if (alreadyOrderedServices.contains(serviceId)) {
                results.put(serviceId, RequestedServiceStatus.ALREADY_ORDERED);
                continue;
            }

            var additionalService = additionalServiceRepo.getAdditionalService(serviceId);
            if (additionalService.isEmpty()) {
                log.warn("Requested service with id - '{}' is not present!", serviceId);
                results.put(serviceId, RequestedServiceStatus.UNKNOWN_ID);
                continue;
            }

            if (!isAdditionalServiceAvailableForReservation(
                    serviceId, additionalService.get().getDailyCapacity(),
                    reservation.getStartDate(), reservation.getDuration())
            ) {
                results.put(serviceId, RequestedServiceStatus.NOT_AVAILABLE);
                continue;
            }

            if (orderedServiceRepo.addOrderedService(reservationId, serviceId)) {
                results.put(serviceId, RequestedServiceStatus.ADDED);
                newServicesTotalCost += additionalService.get().getCost();
            }
        }

        updateReservationValue(reservationId, newServicesTotalCost);
        updateClientCharges(clientId);

        return results;
    }

    private boolean validateReservationDates(int petSpeciesId, String date, int duration) {
        var dailyCapacity = petSystem.getSpeciesCapacity(petSpeciesId);

        Date reservationDate;
        try {
            reservationDate = new SimpleDateFormat("dd-MM-yyyy").parse(date);
        } catch (ParseException e) {
            log.error("Invalid date format - '{}', format should be 'dd-MM-yyyy'", date);
            return false;
        }

        for (var i = 0; i <= duration; i++) {
            var currentDay = LocalDateTime.from(reservationDate.toInstant()).plusDays(i);
            if (reservationRepo.countPetsOfSpeciesOnDate(currentDay.toString(), petSpeciesId) >= dailyCapacity) {
                return false;
            }
        }

        return true;
    }

    private void updateClientCharges(int clientId) {
        clientSystem.updateClientCharges(clientId, reservationRepo.countAllActiveReservationsCostForClient(clientId));
    }

    private boolean isAdditionalServiceAvailableForReservation(
            int serviceId, int dailyCapacity,
            String reservationDate, int reservationDuration
    ) {
        //TODO dates validations to add
        return false;
    }

    private void updateReservationValue(int reservationId, float additionalCosts) {
        reservationRepo.increaseReservationValue(reservationId, additionalCosts);
    }
}
