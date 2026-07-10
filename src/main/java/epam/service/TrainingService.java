package epam.service;

import epam.domain.Training;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface TrainingService {

    void save(Training training);
    Training findTrainingById(Long id);
    List<Training> getTrainingByTrainingTypeName(String trainingTypeName);
    List<Training> selectTraineeTrainings(String traineeUsername, LocalDateTime fromDate, LocalDateTime toDate, String trainingType);
    List<Training> selectTrainerTrainings(String trainerUsername, LocalDateTime fromDate, LocalDateTime toDate);
}
