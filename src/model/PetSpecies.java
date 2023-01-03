package src.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PetSpecies {
    int speciesId;
    String name;
    float dailyCost;
    int capacity;
}
