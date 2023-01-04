package src.backend.systems.pets.repositories;

import src.model.Pet;

import java.util.Collection;

public interface PetRepositoryInterface {

    Collection<Pet> getClientPets(int clientId);

    boolean addNewPet(int clientId, Pet pet);
}
