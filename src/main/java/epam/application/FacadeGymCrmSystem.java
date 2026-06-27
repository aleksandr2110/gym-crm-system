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
import epam.util.TraineeMapper;
import epam.util.TrainerMapper;
import epam.util.TrainingMapper;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class FacadeGymCrmSystem {

    private final TrainerService trainerService;
    private final TraineeService traineeService;
    private final TrainingService trainingService;
    private final TraineeMapper traineeMapper;
    private final TrainerMapper trainerMapper;
    private final TrainingMapper trainingMapper;
    private static final Logger logger = Logger.getLogger(FacadeGymCrmSystem.class.getName());

    public FacadeGymCrmSystem(TrainerService trainerService, TraineeService traineeService,
                              TrainingService trainingService, TraineeMapper traineeMapper,
                              TrainerMapper trainerMapper, TrainingMapper trainingMapper) {
        this.trainerService = trainerService;
        this.traineeService = traineeService;
        this.trainingService = trainingService;
        this.traineeMapper = traineeMapper;
        this.trainerMapper = trainerMapper;
        this.trainingMapper = trainingMapper;
    }

    public Trainee createTrainee(TraineeDTO traineeDTO) {
        logger.info("Creating trainee " + traineeDTO.getFirstName() +
                traineeDTO.getLastName());
        var trainee = traineeMapper.toModel(traineeDTO);
        return traineeService.create(trainee);
    }

    public Trainee updateTrainee(TraineeDTO traineeDTO, Long userId) {
        logger.info("Updating trainee with id " + userId);
        var trainee = traineeMapper.toModel(traineeDTO);
        return traineeService.update(trainee, userId);
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
        var trainer =  trainerMapper.toModel(trainerDto);
        return trainerService.create(trainer);
    }

    public Trainer updateTrainer(TrainerDTO trainerDto, Long userId) {
        logger.info("Facade: Updating trainer with id " + userId);
        var trainer =  trainerMapper.toModel(trainerDto);
        return trainerService.update(trainer, userId);
    }

    public Trainer getTrainer(Long userId) {
        logger.info("Facade: Getting trainer with userId " + userId);
        return trainerService.select(userId);
    }

    public Training createTraining(TrainingDTO trainingDTO) {
        logger.info("Creating training " + trainingDTO.toString());
        var training = trainingMapper.toModel(trainingDTO);
        return trainingService.create(training, trainingDTO.getTrainerId(), trainingDTO.getTraineeIds());
    }

    public Training getTraining(Long trainingId) {
        logger.info("Facade: Getting training with id " + trainingId);
        return trainingService.select(trainingId);
    }
}
