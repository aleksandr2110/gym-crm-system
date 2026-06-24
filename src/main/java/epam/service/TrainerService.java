package epam.service;

import epam.domain.Trainer;
import epam.request.TrainerRequest;

public interface TrainerService {

    Trainer create(TrainerRequest trainerDto);
    Trainer update(TrainerRequest trainerDto, String oldUsername);
    Trainer select(String id);
}
