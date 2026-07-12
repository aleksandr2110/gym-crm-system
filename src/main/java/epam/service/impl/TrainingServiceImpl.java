package epam.service.impl;

import epam.annotation.ExecutionTime;
import epam.domain.Training;
import epam.repository.TraineeRepository;
import epam.repository.TrainerRepository;
import epam.repository.TrainingRepository;
import epam.repository.TrainingTypeRepository;
import epam.service.TrainingService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class TrainingServiceImpl implements TrainingService {

    private final TrainingRepository trainingRepository;
    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;
    private final TrainingTypeRepository trainingTypeRepository;

    public TrainingServiceImpl(TrainingRepository trainingRepository,
                               TraineeRepository traineeRepository,
                               TrainerRepository trainerRepository,
                               TrainingTypeRepository trainingTypeRepository) {
        this.trainingRepository = trainingRepository;
        this.traineeRepository = traineeRepository;
        this.trainerRepository = trainerRepository;
        this.trainingTypeRepository = trainingTypeRepository;
    }

    @ExecutionTime
    @Transactional
    @Override
    public void save(Training trainingRequest) {
        var training = getTraining(trainingRequest);
        training.setTrainingName(trainingRequest.getTrainingName());
        training.setTrainingDate(trainingRequest.getTrainingDate());
        training.setTrainingDuration(trainingRequest.getTrainingDuration());

        trainingRepository.save(training);
    }

    @Override
    public Training findTrainingById(Long id) {
        return trainingRepository.findTrainingById(id);
    }

    @Override
    public List<Training> getTrainingByTrainingTypeName(String trainingTypeName) {
        return trainingRepository.getTrainingByTrainingTypeName(trainingTypeName);
    }

    @Override
    public List<Training> selectTraineeTrainings(String traineeUsername, LocalDateTime fromDate,
                                                 LocalDateTime toDate, String trainingType) {
        return trainingRepository.findTraineeTrainingsByUserNameAndDate(traineeUsername,
                fromDate, toDate, trainingType);
    }

    @Override
    public List<Training> selectTrainerTrainings(String trainerUsername, LocalDateTime fromDate,
                                                 LocalDateTime toDate) {
        return trainingRepository.findTrainerTrainingsByUserNameAndDate(trainerUsername, fromDate, toDate);
    }

    private Training getTraining(Training trainingRequest) {
        var training = new Training();
        var trainee = traineeRepository.findByUsername(trainingRequest.getTrainee().getUserName());
        if (trainee == null) {
            log.warn("Trainee not found with username: {}", trainingRequest.getTrainee().getUserName());
            throw new IllegalArgumentException("Trainee not found with username: "
                    + trainingRequest.getTrainee().getUserName());
        }
        training.setTrainee(trainee);

        var trainer = trainerRepository.findByUsername(trainingRequest.getTrainer().getUserName());
        if (trainer == null) {
            log.warn("Trainer not found with username: {}", trainingRequest.getTrainer().getUserName());
            throw new IllegalArgumentException("Trainer not found with username: "
                    + trainingRequest.getTrainer().getUserName());
        }
        training.setTrainer(trainer);
        training.setTrainingType(trainingTypeRepository.findByName(trainingRequest.getTrainingType().getTrainingTypeName().name()));

        return training;
    }

}
