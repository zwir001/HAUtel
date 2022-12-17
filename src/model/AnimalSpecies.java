package src.model;

import lombok.Value;

@Value
public class AnimalSpecies {
    int speciesId;
    String name;
    float dailyCost;
    int capacity;
}
