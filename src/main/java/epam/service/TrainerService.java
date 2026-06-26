package epam.service;

import epam.domain.Trainer;
import epam.request.TrainerRequest;

public interface TrainerService {

    Trainer create(TrainerRequest trainerDto);
    Trainer update(TrainerRequest trainerDto, Long userId);
    Trainer select(Long id);
}
