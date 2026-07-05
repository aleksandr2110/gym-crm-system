package epam.service;

import epam.domain.Training;

import java.time.LocalDate;
import java.util.List;

public interface TrainingService {

    Training save(Training training);
    Training findTrainingById(Long id);
    List<Training> selectTraineeTrainings(String traineeUsername, LocalDate fromDate, LocalDate toDate, String trainingType);
    List<Training> selectTrainerTrainings(String trainerUsername, LocalDate fromDate, LocalDate toDate);
}
