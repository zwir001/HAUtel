package src.backend.systems.reservations;

import lombok.extern.slf4j.Slf4j;
import src.backend.infrastructure.ConnectionManager;
import src.backend.systems.clients.ClientSystem;
import src.backend.systems.pets.PetSystem;
import src.backend.systems.reservations.repositories.*;
import src.model.Reservation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

@Slf4j
public class ReservationSystem {
    private final ConnectionManager connectionManager;

    private final ClientSystem clientSystem;
    private final PetSystem petSystem;

    private final ReservationRepositoryInterface reservationRepo;
    private final OrderedServiceRepositoryInterface orderedServiceRepo;
    private final ReservationStatusRepositoryInterface reservationStatusRepo;

    public ReservationSystem(ConnectionManager connectionManager, ClientSystem clientSystem, PetSystem petSystem) {
        this.connectionManager = connectionManager;
        this.clientSystem = clientSystem;
        this.petSystem = petSystem;
        this.reservationRepo = new ReservationRepository(connectionManager);
        this.orderedServiceRepo = new OrderedServiceRepository(connectionManager);
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

        return reservationRepo.addNewReservation(Reservation.builder()
                .animalId(petId)
                .startDate(date)
                .duration(duration)
                .value(reservationValue)
                .statusId(reservationStatusRepo.getStatusId(ReservationStatus.ACTIVE.getName()))
                .build()
        );
    }

    public boolean cancelReservation(int reservationId) {
        var cancelledStatusId = reservationStatusRepo.getStatusId(ReservationStatus.CANCELLED.getName());

        reservationRepo.changeReservationStatus(reservationId, cancelledStatusId);

        return orderedServiceRepo.deleteOrderedServicesForReservation(reservationId);
    }

    public void completeReservation(int reservationId) {
        var completedStatusId = reservationStatusRepo.getStatusId(ReservationStatus.COMPLETED.getName());

        reservationRepo.changeReservationStatus(reservationId, completedStatusId);
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
            if (reservationRepo.getCountPetsOfSpeciesFromDate(currentDay.toString(), petSpeciesId) >= dailyCapacity) {
                return false;
            }
        }

        return true;
    }

    private void updateClientCharges(int clientId) {
        // TODO updating client charges
        var charges = 0;

        clientSystem.updateClientCharges(clientId, charges);
    }
}
