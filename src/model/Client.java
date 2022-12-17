package src.model;

import lombok.Value;

@Value
public class Client {
    int id;
    String name;
    String secondName;
    String email;
    String phoneNumber;
    float charges;
}
