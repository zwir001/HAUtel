package src.backend.systems.reservations.repositories;

import src.backend.infrastructure.ConnectionManager;
import src.backend.systems.repositories.AbstractRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class OrderedServiceRepository extends AbstractRepository implements OrderedServiceRepositoryInterface {
    public OrderedServiceRepository(ConnectionManager connectionManager) {
        super(connectionManager);
    }

    @Override
    public Collection<Integer> getOrderedServices(int reservationId) {
        var query = String.format("SELECT dodatkowauslugaid FROM zamowionausluga WHERE rezerwacjaid = %d", reservationId);
        var result = executor.executeSelect(query);

        var services = new ArrayList<Integer>();
        try {
            while (result.next()) {
                services.add(result.getInt("dodatkowauslugaid"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return services;
    }

    @Override
    public boolean addOrderedService(int reservationId, int serviceId) {
        var query = String.format("INSERT INTO zamowionausluga (rezerwacjaid, dodatkowauslugaid) " +
                        "values (%d, %d);",
                reservationId,
                serviceId
        );

        return executor.executeQuery(query);
    }

    @Override
    public boolean deleteOrderedServicesForReservation(int reservationId) {
        var query = String.format("DELETE FROM zamowionausluga WHERE rezerwacjaid = %d", reservationId);
        return executor.executeQuery(query);
    }

    @Override
    public int countOrderedServicesOfTypeForInterval(int serviceId, String date, int duration) {
        var query = String.format("SELECT count(*) AS no " +
                "FROM zamowionausluga z " +
                "JOIN rezerwacja r on r.id = z.rezerwacjaid " +
                "WHERE (r.termin between '%s' and DATE('%s') + interval '%d' day " +
                "OR r.termin + r.czaspobytu between '%s' and DATE('%s') + interval '%d' day) " +
                "AND r.statusrezerwacjiid = 1 " +
                "AND z.dodatkowauslugaid = %d",
                date, date, duration,
                date, date, duration,
                serviceId);

        var result = executor.executeSelect(query);

        try {
            if (result.next()) {
                return result.getInt("no");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return 0;
    }
}
