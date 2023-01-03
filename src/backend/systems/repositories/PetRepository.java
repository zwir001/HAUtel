package src.backend.systems.repositories;

import src.backend.infrastructure.ConnectionManager;
import src.model.Pet;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class PetRepository extends AbstractRepository implements PetRepositoryInterface {
    public PetRepository(ConnectionManager connectionManager) {
        super(connectionManager);
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
    public boolean addNewPet(int clientId, Pet pet) {
        var query = String.format("insert into Zwierze (imie, klientid, gatunekid, leki, alergie)" +
                        "values ('%s', %d, %d, '%s', '%s')",
                pet.getName(),
                clientId,
                pet.getSpeciesId(),
                pet.getDrugs(),
                pet.getAllergy()
        );

        return executor.executeQuery(query);
    }
}
