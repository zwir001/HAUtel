package src.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Client {
    int id;
    String name;
    String secondName;
    String email;
    String phoneNumber;
    float charges;
}
