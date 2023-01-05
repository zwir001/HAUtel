package src.backend.systems.reservations.repositories;

import src.model.Reservation;
import src.model.ReservationClientView;

import java.util.Collection;
import java.util.Optional;

public interface ReservationRepositoryInterface {
    Collection<ReservationClientView> getReservationsForPet(int petId);

    Optional<Reservation> getReservation(int reservationId);

    boolean addNewReservation(Reservation reservation);

    void changeReservationStatus(int reservationId, int statusId);

    int countPetsOfSpeciesOnDate(String date, int speciesId);

    float countAllActiveReservationsCostForClient(int clientId);
}
