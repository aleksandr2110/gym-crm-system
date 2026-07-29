package epam.service;

import epam.domain.dto.request.TraineeTrainingsRequestDTO;
import epam.domain.dto.request.TrainerTrainingsRequestDTO;
import epam.domain.dto.request.TrainingRequestDTO;
import epam.domain.dto.response.TrainingDTO;
import epam.domain.entity.Training;

import java.time.LocalDateTime;
import java.util.List;

public interface TrainingService {

    void save(Training training);
    Training findTrainingById(Long id);
    List<Training> getTrainingByTrainingTypeName(String trainingTypeName);
    List<Training> selectTraineeTrainings(String username, LocalDateTime fromDate, LocalDateTime toDate,
                                          String trainingType);
    List<Training> selectTrainerTrainings(String username, LocalDateTime fromDate, LocalDateTime toDate);
}
