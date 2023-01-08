package src.backend.systems.pets.repositories;

import src.model.PetSpecies;

import java.util.Optional;

public interface PetSpeciesRepositoryInterface {
    Optional<PetSpecies> getSpeciesInfo(int id);

    int getSpeciesCapacity(int id);

    float getSpeciesDailyCost(int id);

    boolean speciesExists(int id);
}
