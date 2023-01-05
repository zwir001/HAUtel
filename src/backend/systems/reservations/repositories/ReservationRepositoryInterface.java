package src.backend.systems.reservations.repositories;

import src.model.Reservation;

import java.util.Collection;

public interface ReservationRepositoryInterface {
    Collection<Reservation> getReservationsForPet(int petId);

    boolean addNewReservation(Reservation reservation);

    void changeReservationStatus(int reservationId, int statusId);

    int getCountPetsOfSpeciesFromDate(String date, int speciesId);
}
