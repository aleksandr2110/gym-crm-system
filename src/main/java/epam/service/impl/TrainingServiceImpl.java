package epam.service.impl;

import epam.annotation.ExecutionTime;
import epam.domain.Trainee;
import epam.domain.Training;
import epam.repository.TraineeRepository;
import epam.repository.TrainerRepository;
import epam.repository.TrainingRepository;
import epam.service.TrainingService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class TrainingServiceImpl implements TrainingService {

    private final TrainingRepository trainingRepository;
    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;
    private static final Logger logger = Logger.getLogger(TrainingServiceImpl.class.getName());

    public TrainingServiceImpl(TrainingRepository trainingRepository,
                               TraineeRepository traineeRepository,
                               TrainerRepository trainerRepository) {
        this.trainingRepository = trainingRepository;
        this.traineeRepository = traineeRepository;
        this.trainerRepository = trainerRepository;
    }

    @Override
    @ExecutionTime
    public Training create(Training training, Long trainerId, List<Long> traineeIds) {
        training.setTrainer(trainerRepository.select(trainerId));
        List<Trainee> traineeList = new ArrayList();
        for (Long traineeId : traineeIds) {
            traineeList.add(traineeRepository.select(traineeId));
        }
        training.setTrainers(traineeList);
        var createdTraining = trainingRepository.save(training);

        if (createdTraining == null) {
            logger.warning("Failed to create training: ");
        } else {
            logger.info("Training created successfully with id : " + createdTraining.getId());
        }

        return createdTraining;
    }

    @Override
    @ExecutionTime
    public Training select(Long trainingId) {
        Training training = trainingRepository.select(trainingId);

        if (training == null) {
            logger.warning("Training not found with id: " + trainingId);
        } else {
            logger.info("Training found with id: " + trainingId);
        }

        return training;
    }
}
