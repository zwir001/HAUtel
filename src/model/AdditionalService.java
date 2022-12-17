package src.model;

import lombok.Value;

@Value
public class AdditionalService {
    int id;
    String name;
    float dailyCost;
    int dailyCapacity;
}
