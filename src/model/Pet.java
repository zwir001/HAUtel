package src.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Pet {
    int id;
    String name;
    int ownerId;
    int speciesId;
    String drugs;
    String allergy;
}
