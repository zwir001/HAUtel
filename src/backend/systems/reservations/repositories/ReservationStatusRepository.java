package src.backend.systems.reservations.repositories;

import src.backend.infrastructure.ConnectionManager;
import src.backend.systems.repositories.AbstractRepository;

import java.sql.SQLException;

public class ReservationStatusRepository extends AbstractRepository implements ReservationStatusRepositoryInterface {
    public ReservationStatusRepository(ConnectionManager connectionManager) {
        super(connectionManager);
    }

    @Override
    public int getStatusId(String statusName) {
        var query = String.format("SELECT id FROM statusrezerwacji WHERE nazwa = '%s'", statusName);

        var result = executor.executeSelect(query);

        try {
            if(result.next()) {
                return result.getInt("id");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return 0;
    }
}
