package src.backend.systems.reservations.repositories;

import src.backend.infrastructure.ConnectionManager;
import src.backend.systems.repositories.AbstractRepository;
import src.model.Reservation;

import java.sql.SQLException;
import java.util.Collection;

public class ReservationRepository extends AbstractRepository implements ReservationRepositoryInterface {
    public ReservationRepository(ConnectionManager connectionManager) {
        super(connectionManager);
    }

    @Override
    public Collection<Reservation> getReservationsForPet(int petId) {
        return null;
    }

    @Override
    public boolean addNewReservation(Reservation reservation) {
        var query = String.format("INSERT INTO rezerwacja (zwierzeID, termin, czasPobytu, kosztPobytu, statusRezerwacjiID)" +
                        "VALUES (%d, '%s', %d, %f, %d)",
                reservation.getAnimalId(),
                reservation.getStartDate(),
                reservation.getDuration(),
                reservation.getValue(),
                reservation.getStatusId()
        );

        return executor.executeQuery(query);
    }


    @Override
    public void changeReservationStatus(int reservationId, int statusId) {
        var query = String.format("UPDATE rezerwacja SET statusrezerwacjiid = %d WHERE id = %d", reservationId, statusId);
        executor.executeUpdate(query);
    }

    @Override
    public int getCountPetsOfSpeciesFromDate(String date, int speciesId) {
        var query = String.format("SELECT count(*) AS no" +
                "FROM rezerwacja" +
                "JOIN zwierze z" +
                "ON rezerwacja.zwierzeid = z.id" +
                "WHERE '%s' BETWEEN termin AND termin + czaspobytu" +
                "AND z.gatunekid = %d" +
                "AND statusrezerwacjiid = 1" +
                "GROUP BY gatunekid", date, speciesId);

        var result = executor.executeSelect(query);

        try {
            if(result.next()) {
                return result.getInt("no");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return 0;
    }
}
