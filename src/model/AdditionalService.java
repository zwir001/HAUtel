package src.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AdditionalService {
    int id;
    String name;
    float cost;
    int dailyCapacity;
}
