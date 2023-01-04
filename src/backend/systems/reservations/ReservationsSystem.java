package src.backend.systems.reservations;

import src.backend.infrastructure.ConnectionManager;
import src.backend.systems.reservations.repositories.ReservationRepositoryInterface;

public class ReservationsSystem {
    private final ConnectionManager connectionManager;

    private ReservationRepositoryInterface reservationRepo;

    public ReservationsSystem(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }
}
