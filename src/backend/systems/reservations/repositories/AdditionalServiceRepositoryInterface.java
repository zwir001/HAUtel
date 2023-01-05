package src.backend.systems.reservations.repositories;

import src.model.AdditionalService;

import java.util.Optional;

public interface AdditionalServiceRepositoryInterface {
    Optional<AdditionalService> getAdditionalService(int serviceId);
}
