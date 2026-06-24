package epam.application;

import epam.domain.InnerDataTraining;
import epam.domain.Trainee;
import epam.domain.Trainer;
import epam.domain.Training;
import epam.request.TraineeRequest;
import epam.request.TrainerRequest;
import epam.request.TrainingRequest;
import epam.service.TraineeService;
import epam.service.TrainerService;
import epam.service.TrainingService;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class FacadeGymCrmSystem {

    private final TrainerService trainerService;
    private final TraineeService traineeService;
    private final TrainingService trainingService;
    private static final Logger logger = Logger.getLogger(FacadeGymCrmSystem.class.getName());

    public FacadeGymCrmSystem(TrainerService trainerService, TraineeService traineeService,
                              TrainingService trainingService) {
        this.trainerService = trainerService;
        this.traineeService = traineeService;
        this.trainingService = trainingService;
    }

    public Trainee createTrainee(TraineeRequest traineeRequest) {
        logger.info("Creating trainee " + traineeRequest.getFirstName() +
                traineeRequest.getLastName());
        return traineeService.create(traineeRequest);
    }

    public Trainee updateTrainee(TraineeRequest traineeRequest, String userId) {
        logger.info("Updating trainee with id " + userId);
        return traineeService.update(traineeRequest, userId);
    }

    public Trainee getTrainee(String userId) {
        logger.info("Getting trainee with userId " + userId);
        return traineeService.select(userId);
    }

    public void deleteTrainee(String userId) {
        logger.info("Deleting trainee with userId " + userId);
        traineeService.delete(userId);
    }

    public Trainer createTrainer(TrainerRequest trainerDto) {
        logger.info("Creating trainer " + trainerDto.getFirstName() + " " + trainerDto.getLastName());
        return trainerService.create(trainerDto);
    }

    public Trainer updateTrainer(TrainerRequest trainerDto, String username) {
        logger.info("Facade: Updating trainer with username " + username);
        return trainerService.update(trainerDto, username);
    }

    public Trainer getTrainer(String userId) {
        logger.info("Facade: Getting trainer with userId " + userId);
        return trainerService.select(userId);
    }

    public Training createTraining(TrainingRequest trainingCreateRequest) {
        logger.info("Creating training " +
                trainingCreateRequest.getInnerDataTraining().toString());
        return trainingService.create(trainingCreateRequest);
    }

    public Training getTraining(InnerDataTraining trainingId) {
        logger.info("Facade: Getting training with id " + trainingId);
        return trainingService.select(trainingId);
    }
}
