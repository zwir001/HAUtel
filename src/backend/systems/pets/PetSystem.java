package src.backend.systems.pets;

import lombok.extern.slf4j.Slf4j;
import src.backend.infrastructure.ConnectionManager;
import src.backend.infrastructure.DatabaseUser;
import src.backend.systems.pets.repositories.PetRepository;
import src.backend.systems.pets.repositories.PetRepositoryInterface;
import src.backend.systems.pets.repositories.PetSpeciesRepository;
import src.backend.systems.pets.repositories.PetSpeciesRepositoryInterface;
import src.model.Pet;
import src.model.exceptions.InvalidPetDataException;
import src.model.factories.PetFactory;

import java.util.Collection;
import java.util.Optional;

@Slf4j
public class PetSystem {
    private final ConnectionManager connectionManager;

    private final PetRepositoryInterface petRepo;
    private final PetSpeciesRepositoryInterface speciesRepo;

    public PetSystem() {
        this.connectionManager = DatabaseUser.CLIENT.getConnectionManager();
        this.petRepo = new PetRepository(this.connectionManager);
        this.speciesRepo = new PetSpeciesRepository(this.connectionManager);
    }

    public PetSystem(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
        this.petRepo = new PetRepository(connectionManager);
        this.speciesRepo = new PetSpeciesRepository(connectionManager);
    }

    public boolean addNewPet(int clientId, String name, int speciesId, String drugs, String allergy) {
        if (!speciesRepo.speciesExists(speciesId)) {
            log.error("Species - '{}' of new animal is not available", speciesId);
            return false;
        }
        try {
            var pet = PetFactory.createNewPet(name, clientId, speciesId, drugs, allergy);
            return petRepo.addNewPet(clientId, pet);
        } catch (InvalidPetDataException e) {
            log.error(e.getMessage());
            return false;
        }
    }

    public Optional<Pet> getPet(int petId) {
        return petRepo.getPet(petId);
    }

    public Collection<Pet> getAllClientPets(int clientId) {
        return petRepo.getClientPets(clientId);
    }

    public Collection<Integer> getAllClientPetIds(int clientId) {
        return petRepo.getClientPetIds(clientId);
    }

    public float getSpeciesDailyCost(int speciesId) {
        return speciesRepo.getSpeciesDailyCost(speciesId);
    }

    public int getSpeciesCapacity(int speciesId) {
        return speciesRepo.getSpeciesCapacity(speciesId);
    }
}
