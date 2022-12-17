package src.model;

import lombok.Value;

import java.time.Instant;

@Value
public class Reservation {
    int id;
    int animalId;
    Instant startDate;
    int duration;
    float value;
    int statusId;
}
