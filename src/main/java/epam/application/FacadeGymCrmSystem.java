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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

@Slf4j
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
        log.info("Creating trainee {} {}", traineeDTO.getFirstName(), traineeDTO.getLastName());
        var trainee = traineeMapper.toModel(traineeDTO);
        return traineeService.save(trainee);
    }

    public boolean authenticateTrainee(String username, String password) {
        log.info("Authenticating trainee {}", username);
        return traineeService.authenticateTrainee(username, password);
    }

    public Trainee updateTrainee(TraineeDTO traineeDTO, Long userId) {
        log.info("Updating trainee with id {}", userId);
        var trainee = traineeMapper.toModel(traineeDTO);
        return traineeService.updateProfile(trainee, userId);
    }

    public Trainee getTrainee(Long userId) {
        logger.info("Getting trainee with userId " + userId);
        return traineeService.findById(userId);
    }

    public void deleteTrainee(String username) {
        log.info("Deleting trainee with user name {}", username);
        traineeService.deleteProfile(username);
    }

    public Trainer createTrainer(TrainerDTO trainerDto) {
        logger.info("Creating trainer " + trainerDto.getFirstName() + " " + trainerDto.getLastName());
        var trainer =  trainerMapper.toModel(trainerDto);
        return trainerService.save(trainer);
    }

    public Trainer updateTrainer(TrainerDTO trainerDto, Long userId) {
        logger.info("Updating trainer with id " + userId);
        var trainer =  trainerMapper.toModel(trainerDto);
        return trainerService.updateProfile(trainer);
    }

    public Trainer getTrainer(Long userId) {
        logger.info("Facade: Getting trainer with userId " + userId);
        return trainerService.findById(userId);
    }

    public Training createTraining(TrainingDTO trainingDTO) {
        log.info("Creating training {}", trainingDTO.toString());
        var training = trainingMapper.toModel(trainingDTO);
        return trainingService.save(training);
    }

    public Training findById(Long id) {
        log.info("Find training by id {}", id);
        return trainingService.findTrainingById(id);
    }

    public List<Training> getTraineeTrainingByUserNameDateAndTrainingType(String traineeUsername, LocalDate fromDate,
                                              LocalDate toDate, String trainingType) {
        log.info("Getting trainings for trainee {} from {} to {}", traineeUsername, fromDate, toDate);
        return trainingService.selectTraineeTrainings(traineeUsername, fromDate, toDate, trainingType);
    }

    public List<Training> getTrainerTrainingsByUserNameDateAndTrainingType(String trainerUsername, LocalDate fromDate,
                                                                           LocalDate toDate) {
        log.info("Getting trainings for trainer {} from {} to {}", trainerUsername, fromDate, toDate);
        return trainingService.selectTrainerTrainings(trainerUsername, fromDate, toDate);
    }

    public List<Trainer> getTrainersNotAssignedToTrainee(String traineeUsername) {
        log.info("Getting trainers not assigned to trainee {}", traineeUsername);
        return trainerService.findAllNotAssignedToTrainee(traineeUsername);
    }
}
