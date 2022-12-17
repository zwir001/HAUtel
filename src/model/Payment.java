package src.model;

import lombok.Value;

@Value
public class Payment {
    int id;
    int reservationId;
    String bankAccount;
    String name;
    String secondName;
    float value;
}
