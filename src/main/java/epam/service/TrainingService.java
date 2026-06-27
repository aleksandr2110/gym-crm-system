package epam.service;

import epam.domain.Training;
import epam.request.TrainingDTO;

public interface TrainingService {

    Training create(TrainingDTO trainingDTO);
    Training select(Long id);
}
