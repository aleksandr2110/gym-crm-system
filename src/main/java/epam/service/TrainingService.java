package epam.service;

import epam.domain.Trainee;
import epam.domain.Training;
import epam.request.TrainingDTO;

import java.util.List;

public interface TrainingService {

    Training create(Training training, Long trainerId, List<Long> traineeIds);
    Training select(Long id);
}
