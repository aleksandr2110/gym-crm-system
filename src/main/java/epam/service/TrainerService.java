package epam.service;

import epam.domain.Trainer;

public interface TrainerService {

    Trainer create(Trainer trainer);
    Trainer update(Trainer trainer, Long userId);
    Trainer select(Long id);
}
