package src.backend.systems.repositories;

import java.util.Optional;

public interface ClientLoginRepositoryInterface {

    Optional<Integer> getClientIdFromEmailAddress(String emailAddress);
}
