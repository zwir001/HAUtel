package src.backend.systems.reservations;

import src.backend.infrastructure.ConnectionManager;
import src.backend.systems.reservations.repositories.ReservationRepositoryInterface;

public class ReservationSystem {
    private final ConnectionManager connectionManager;

    private ReservationRepositoryInterface reservationRepo;

    public ReservationSystem(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }
}
