package epam.application;

import epam.domain.Trainee;
import epam.domain.Trainer;
import epam.domain.Training;
import epam.request.TraineeDTO;
import epam.request.TrainerDTO;
import epam.request.TrainingDTO;
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

    public Trainee createTrainee(TraineeDTO traineeDTO) {
        logger.info("Creating trainee " + traineeDTO.getFirstName() +
                traineeDTO.getLastName());
        return traineeService.create(traineeDTO);
    }

    public Trainee updateTrainee(TraineeDTO traineeDTO, Long userId) {
        logger.info("Updating trainee with id " + userId);
        return traineeService.update(traineeDTO, userId);
    }

    public Trainee getTrainee(Long userId) {
        logger.info("Getting trainee with userId " + userId);
        return traineeService.select(userId);
    }

    public void deleteTrainee(Long userId) {
        logger.info("Deleting trainee with userId " + userId);
        traineeService.delete(userId);
    }

    public Trainer createTrainer(TrainerDTO trainerDto) {
        logger.info("Creating trainer " + trainerDto.getFirstName() + " " + trainerDto.getLastName());
        return trainerService.create(trainerDto);
    }

    public Trainer updateTrainer(TrainerDTO trainerDto, Long userId) {
        logger.info("Facade: Updating trainer with id " + userId);
        return trainerService.update(trainerDto, userId);
    }

    public Trainer getTrainer(Long userId) {
        logger.info("Facade: Getting trainer with userId " + userId);
        return trainerService.select(userId);
    }

    public Training createTraining(TrainingDTO trainingDTO) {
        logger.info("Creating training " + trainingDTO.toString());
        return trainingService.create(trainingDTO);
    }

    public Training getTraining(Long trainingId) {
        logger.info("Facade: Getting training with id " + trainingId);
        return trainingService.select(trainingId);
    }
}
