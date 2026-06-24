package epam.service.impl;

import epam.domain.InnerDataTraining;
import epam.domain.Training;
import epam.repository.TrainingRepository;
import epam.request.TrainingRequest;
import epam.service.TrainingService;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class TrainingServiceImpl implements TrainingService {

    private final TrainingRepository trainingRepository;
    private static final Logger logger = Logger.getLogger(TrainingServiceImpl.class.getName());

    public TrainingServiceImpl(TrainingRepository trainingRepository) {
        this.trainingRepository = trainingRepository;
    }

    @Override
    public Training create(TrainingRequest trainingRequest) {
        logger.info("Creating training: " + trainingRequest.getInnerDataTraining().toString());

        var training = new Training();
        training.setInnerDataTraining(trainingRequest.getInnerDataTraining());
        training.setTrainingType(trainingRequest.getTrainingType());
        training.setTrainingDate(trainingRequest.getTrainingDate());
        training.setTrainingDuration(trainingRequest.getTrainingDuration());

        Training createdTraining = trainingRepository.save(training);

        if (createdTraining == null) {
            logger.warning("Failed to create training: " + trainingRequest.getInnerDataTraining());
        } else {
            logger.info("Training created successfully: " + createdTraining.getInnerDataTraining());
        }

        return createdTraining;
    }

    @Override
    public Training select(InnerDataTraining id) {
        logger.info("Selecting training by id: " + id.toString());
        Training training = trainingRepository.select(id);

        if (training == null) {
            logger.warning("Training not found with id: " + id.toString());
        } else {
            logger.info("Training found with id: " + id.toString());
        }

        return training;
    }
}
