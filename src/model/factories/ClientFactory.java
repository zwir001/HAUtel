package src.model.factories;

import lombok.experimental.UtilityClass;
import src.model.Client;
import src.model.exceptions.InvalidClientDataException;

import java.util.Optional;

@UtilityClass
public class ClientFactory {
    public Client createNewClient (
            String name,
            String secondName,
            String email,
            String phoneNumber) throws InvalidClientDataException {
        var builder = Client.builder();

        builder.name(Optional.ofNullable(name).orElseThrow(() -> new InvalidClientDataException("name")));
        builder.secondName(Optional.ofNullable(secondName).orElseThrow(() -> new InvalidClientDataException("secondName")));
        builder.email(Optional.ofNullable(email).orElseThrow(() -> new InvalidClientDataException("email")));
        builder.phoneNumber(Optional.ofNullable(phoneNumber).orElseThrow(() -> new InvalidClientDataException("phoneNumber")));

        return builder.build();
    }
}
