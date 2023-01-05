package src.backend.systems.reservations.repositories;

import java.util.Collection;

public interface OrderedServiceRepositoryInterface {
    Collection<Integer> getOrderedServices(int reservationId);
    boolean addOrderedService(int reservationId, int serviceId);
    boolean deleteOrderedServicesForReservation(int reservationId);
}
