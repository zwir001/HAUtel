package src.backend.systems.pets.repositories;

import src.backend.infrastructure.ConnectionManager;
import src.backend.systems.repositories.AbstractRepository;
import src.model.PetSpecies;

import java.sql.SQLException;
import java.util.Optional;

public class PetSpeciesRepository extends AbstractRepository implements PetSpeciesRepositoryInterface {
    public PetSpeciesRepository(ConnectionManager connectionManager) {
        super(connectionManager);
    }

    @Override
    public Optional<PetSpecies> getSpeciesInfo(int id) {
        var query = String.format("SELECT * FROM gatunek WHERE id = %d", id);
        var result = executor.executeSelect(query);

        try {
            if (result.next()) {
                return Optional.of(PetSpecies.builder()
                        .speciesId(result.getInt("id"))
                        .name(result.getString("nazwa"))
                        .dailyCost(result.getFloat("dziennykoszt"))
                        .capacity(result.getInt("limitmiejsc"))
                        .build()
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }

    @Override
    public boolean speciesExists(int id) {
        var query = String.format("SELECT id FROM gatunek WHERE id = %d", id);
        var result = executor.executeSelect(query);

        try {
            return result.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
