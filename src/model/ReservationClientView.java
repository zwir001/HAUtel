package src.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ReservationClientView {
    int id;
    int animalId;
    String startDate;
    int duration;
    float value;
    String status;
}
