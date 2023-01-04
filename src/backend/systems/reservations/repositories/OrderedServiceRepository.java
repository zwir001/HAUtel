package src.backend.systems.reservations.repositories;

import src.backend.infrastructure.ConnectionManager;
import src.backend.systems.repositories.AbstractRepository;

public class OrderedServiceRepository extends AbstractRepository implements OrderedServiceRepositoryInterface {
    public OrderedServiceRepository(ConnectionManager connectionManager) {
        super(connectionManager);
    }

    @Override
    public boolean deleteOrderedServicesForReservation(int reservationId) {
        var query = String.format("DELETE FROM zamowionausluga WHERE rezerwacjaid = %d", reservationId);
        return executor.executeQuery(query);
    }
}
