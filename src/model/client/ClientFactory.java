package src.model.client;

import lombok.experimental.UtilityClass;

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
        builder.name(Optional.ofNullable(secondName).orElseThrow(() -> new InvalidClientDataException("secondName")));
        builder.name(Optional.ofNullable(email).orElseThrow(() -> new InvalidClientDataException("email")));
        builder.name(Optional.ofNullable(phoneNumber).orElseThrow(() -> new InvalidClientDataException("phoneNumber")));

        return builder.build();
    }
}
