package epam.service;

import epam.domain.Trainer;
import epam.request.TrainerDTO;

public interface TrainerService {

    Trainer create(TrainerDTO trainerDto);
    Trainer update(TrainerDTO trainerDto, Long userId);
    Trainer select(Long id);
}
