package src.model;

import lombok.Value;

@Value
public class Animal {
    int id;
    String name;
    int ownerId;
    int speciesId;
    String drugs;
    String allergy;
}
