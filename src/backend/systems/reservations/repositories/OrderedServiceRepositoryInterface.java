package src.backend.systems.reservations.repositories;

public interface OrderedServiceRepositoryInterface {
    boolean deleteOrderedServicesForReservation(int reservationId);
}
