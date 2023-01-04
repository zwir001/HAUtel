package src.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Reservation {
    int id;
    int animalId;
    String startDate;
    int duration;
    float value;
    int statusId;
}
