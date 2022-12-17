package src.model;

import lombok.Value;

@Value
public class OrderedService {
    int id;
    int reservationId;
    int additionalService;
}
