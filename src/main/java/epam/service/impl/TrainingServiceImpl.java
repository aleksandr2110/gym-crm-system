package epam.service.impl;

import epam.domain.Trainee;
import epam.domain.Training;
import epam.repository.TraineeRepository;
import epam.repository.TrainerRepository;
import epam.repository.TrainingRepository;
import epam.request.TrainingDTO;
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
    public Training create(TrainingDTO trainingDTO) {
        var training = new Training();
        training.setTrainer(trainerRepository.select(trainingDTO.getTrainerId()));
        List<Trainee> traineeList = new ArrayList();
        for (Long traineeId : trainingDTO.getTraineeIds()) {
            traineeList.add(traineeRepository.select(traineeId));
        }
        training.setTrainers(traineeList);
        training.setTrainingName(trainingDTO.getTrainingName());
        training.setTrainingType(trainingDTO.getTrainingType());
        training.setTrainingDate(trainingDTO.getTrainingDate());
        training.setTrainingDuration(trainingDTO.getTrainingDuration());

        Training createdTraining = trainingRepository.save(training);

        if (createdTraining == null) {
            logger.warning("Failed to create training: ");
        } else {
            logger.info("Training created successfully: ");
        }

        return createdTraining;
    }

    @Override
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
