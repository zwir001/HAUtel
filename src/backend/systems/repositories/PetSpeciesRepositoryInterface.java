package src.backend.systems.repositories;

import src.model.PetSpecies;

import java.util.Optional;

public interface PetSpeciesRepositoryInterface {
    Optional<PetSpecies> getSpeciesInfo(int id);

    boolean speciesExists(int id);
}
