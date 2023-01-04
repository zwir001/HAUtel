package src.backend.systems.pets.repositories;

import src.model.Pet;

import java.util.Collection;
import java.util.Optional;

public interface PetRepositoryInterface {

    Optional<Pet> getPet(int petId);

    Collection<Pet> getClientPets(int clientId);

    Collection<Integer> getClientPetIds(int clientId);

    int getPetSpeciesId(int petId);

    boolean addNewPet(int clientId, Pet pet);
}
