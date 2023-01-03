package src.model.factories;

import lombok.experimental.UtilityClass;
import src.model.Pet;
import src.model.exceptions.InvalidPetDataException;

import java.util.Optional;

@UtilityClass
public class PetFactory {
    public Pet createNewPet(
            String name,
            Integer ownerId,
            Integer speciesId,
            String drugs,
            String allergy
    ) throws InvalidPetDataException {
        var builder = Pet.builder();

        builder.name(Optional.ofNullable(name).orElseThrow(() -> new InvalidPetDataException("name")));
        builder.ownerId(Optional.ofNullable(ownerId).orElseThrow(() -> new InvalidPetDataException("ownerId")));
        builder.speciesId(Optional.ofNullable(speciesId).orElseThrow(() -> new InvalidPetDataException("speciesId")));

        if (drugs != null) {
            builder.drugs(drugs);
        }

        if (allergy != null) {
            builder.allergy(allergy);
        }

        return builder.build();
    }
}
