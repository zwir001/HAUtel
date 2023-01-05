package src.backend.systems.reservations.repositories;

import src.backend.infrastructure.ConnectionManager;
import src.backend.systems.repositories.AbstractRepository;
import src.model.AdditionalService;

import java.sql.SQLException;
import java.util.Optional;

public class AdditionalServiceRepository extends AbstractRepository implements AdditionalServiceRepositoryInterface {
    public AdditionalServiceRepository(ConnectionManager connectionManager) {
        super(connectionManager);
    }

    @Override
    public Optional<AdditionalService> getAdditionalService(int serviceId) {
        var query = String.format("SELECT * FROM dodatkowausluga WHERE id = %d", serviceId);
        var result = executor.executeSelect(query);

        try {
            if (result.next()) {
                return Optional.of(AdditionalService.builder()
                        .id(result.getInt("id"))
                        .name(result.getString("nazwa"))
                        .cost(result.getFloat("koszt"))
                        .dailyCapacity(result.getInt("dziennylimitmiejsc"))
                        .build());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }
}
