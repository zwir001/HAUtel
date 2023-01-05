package src.backend.systems.pets.repositories;

import src.backend.infrastructure.ConnectionManager;
import src.backend.systems.repositories.AbstractRepository;
import src.model.Pet;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class PetRepository extends AbstractRepository implements PetRepositoryInterface {
    public PetRepository(ConnectionManager connectionManager) {
        super(connectionManager);
    }

    @Override
    public Optional<Pet> getPet(int petId) {
        var query = String.format("SELECT id, imie, klientid, gatunekid, leki, alergie FROM zwierze WHERE id = %d", petId);
        var result = executor.executeSelect(query);

        try {
            if (result.next()) {
                return Optional.of(Pet.builder()
                        .id(result.getInt("id"))
                        .name(result.getString("imie"))
                        .ownerId(result.getInt("klientid"))
                        .speciesId(result.getInt("gatunekid"))
                        .drugs(result.getString("leki"))
                        .allergy(result.getString("alergie"))
                        .build()
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }

    @Override
    public Collection<Pet> getClientPets(int clientId) {
        var query = String.format("SELECT id, imie, gatunekid, leki, alergie FROM zwierze WHERE klientid = %d", clientId);
        var result = executor.executeSelect(query);

        var petList = new ArrayList<Pet>();
        try {
            while (result.next()) {
                petList.add(Pet.builder()
                        .id(result.getInt("id"))
                        .name(result.getString("imie"))
                        .speciesId(result.getInt("gatunekid"))
                        .drugs(result.getString("leki"))
                        .allergy(result.getString("alergie"))
                        .ownerId(clientId)
                        .build()
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return petList;
    }

    @Override
    public Collection<Integer> getClientPetIds(int clientId) {
        var query = String.format("SELECT id FROM zwierze WHERE klientid = %d", clientId);
        var result = executor.executeSelect(query);

        var petIds = new ArrayList<Integer>();
        try {
            while (result.next()) {
                petIds.add(result.getInt("id"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return petIds;
    }

    @Override
    public int getPetSpeciesId(int petId) {
        var query = String.format("SELECT gatunekid FROM zwierze WHERE id = %d", petId);
        var result = executor.executeSelect(query);

        try {
            if (result.next()) {
                return result.getInt("gatunekId");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return 0;
    }

    @Override
    public boolean addNewPet(int clientId, Pet pet) {
        var query = String.format("INSERT INTO Zwierze (imie, klientid, gatunekid, leki, alergie) " +
                        "VALUES ('%s', %d, %d, '%s', '%s')",
                pet.getName(),
                clientId,
                pet.getSpeciesId(),
                pet.getDrugs(),
                pet.getAllergy()
        );

        return executor.executeQuery(query);
    }
}
