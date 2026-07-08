package epam.application;

import epam.domain.Trainee;
import epam.domain.Trainer;
import epam.domain.Training;
import epam.domain.TrainingType;
import epam.exception.UnauthorizedException;
import epam.request.TraineeDTO;
import epam.request.TrainerDTO;
import epam.request.TrainingDTO;
import epam.request.TrainingTypeDTO;
import epam.service.TraineeService;
import epam.service.TrainerService;
import epam.service.TrainingService;
import epam.service.TrainingTypeService;
import epam.util.TraineeMapper;
import epam.util.TrainerMapper;
import epam.util.TrainingMapper;
import epam.util.TrainingTypeMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
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
    private final TrainingTypeMapper trainingTypeMapper;
    private final TrainingTypeService trainingTypeService;

    private static final Logger logger = Logger.getLogger(FacadeGymCrmSystem.class.getName());

    @Autowired
    public FacadeGymCrmSystem(TrainerService trainerService, TraineeService traineeService,
                              TrainingService trainingService, TraineeMapper traineeMapper,
                              TrainerMapper trainerMapper, TrainingMapper trainingMapper,
                              TrainingTypeMapper trainingTypeMapper,
                              TrainingTypeService trainingTypeService) {
        this.trainerService = trainerService;
        this.traineeService = traineeService;
        this.trainingService = trainingService;
        this.traineeMapper = traineeMapper;
        this.trainerMapper = trainerMapper;
        this.trainingMapper = trainingMapper;
        this.trainingTypeMapper = trainingTypeMapper;
        this.trainingTypeService = trainingTypeService;
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

    public Trainee updateTrainee(TraineeDTO traineeDTO, String username, String password, Long userId) {
        log.info("Updating trainee with username {} {}", username, password);
        if (!authenticateTrainee(username, password)) {
            log.warn("Unauthorized access denied for user name {}", username);
            throw new UnauthorizedException("User is not authenticated");
        }
        var trainee = traineeMapper.toModel(traineeDTO);

        return traineeService.updateProfile(trainee, userId);
    }

    public Trainee getTrainee(String username, String password, Long userId) {
        log.info("Getting trainee with userId {}", userId);
        if (!authenticateTrainee(username, password)) {
            log.warn("Unauthorized access denied for user name {}", username);
            throw new UnauthorizedException("User is not authenticated");
        }
        return traineeService.findById(userId);
    }

    public void deleteTrainee(String username, String password) {
        log.info("Deleting trainee with user name {}", username);
        if (!authenticateTrainee(username, password)) {
            log.warn("Unauthorized access denied for user name {}", username);
            throw new UnauthorizedException("User is not authenticated");
        }
        traineeService.deleteProfile(username);
    }

    public void activateTrainee(String username, String password, Long id) {
        log.info("Activating trainee with user name {}", username);
        if (!authenticateTrainee(username, password)) {
            log.warn("Unauthorized access denied for user name {}", username);
            throw new UnauthorizedException("User is not authenticated");
        }
        traineeService.activate(id);
    }

    public void deactivateTrainee(String username, String password, Long id) {
        log.info("Deactivating trainee with user name {}", username);
        if (!authenticateTrainee(username, password)) {
            log.warn("Unauthorized access denied for user name {}", username);
            throw new UnauthorizedException("User is not authenticated");
        }
        traineeService.deactivate(id);
    }

    public void changeTraineePassword(String username, String password, String newPassword) {
        log.info("Changing password trainee with user name {}", username);
        if (!authenticateTrainee(username, password)) {
            log.warn("Unauthorized access denied for user name {}", username);
            throw new UnauthorizedException("User is not authenticated");
        }
        traineeService.changePassword(username, newPassword);
    }

    public void createTrainingType(List<TrainingTypeDTO> trainingTypeDTOs) {
        log.info("Creating training types ");
        List<TrainingType> trainingTypeList = new ArrayList<>();
        for (TrainingTypeDTO trainingTypeDTO: trainingTypeDTOs) {
            var trainingType = trainingTypeMapper.toModel(trainingTypeDTO);
            trainingTypeList.add(trainingType);
        }
        trainingTypeService.saveTrainingType(trainingTypeList);
    }

    public Trainer createTrainer(TrainerDTO trainerDto) {
        log.info("Creating trainer {} {}", trainerDto.getFirstName(), trainerDto.getLastName());
        var trainer = trainerMapper.toModel(trainerDto);
        return trainerService.save(trainer);
    }

    public boolean authenticateTrainer(String username, String password) {
        log.info("Authenticating trainee {}", username);
        return trainerService.authenticateTrainer(username, password);
    }

    public Trainer getTrainer(String userName, String password, Long userId) {
        log.info("Getting trainer with userId " + userId);
        if (!authenticateTrainer(userName, password)) {
            log.warn("Unauthorized access denied for user name {}", userName);
            throw new UnauthorizedException("User is not authenticated");
        }
        return trainerService.findById(userId);
    }

    public Trainer updateTrainer(String userName, String password, TrainerDTO trainerDto, Long userId) {
        logger.info("Updating trainer with id " + userId);
        if (!authenticateTrainer(userName, password)) {
            log.warn("Unauthorized access denied for user name {}", userName);
            throw new UnauthorizedException("User is not authenticated");
        }
        var trainer =  trainerMapper.toModel(trainerDto);
        return trainerService.updateProfile(trainer, userId);
    }

    public void activateTrainer(String username, String password, Long id) {
        log.info("Activating trainer with user name {}", username);
        if (!authenticateTrainer(username, password)) {
            log.warn("Unauthorized access denied for user name {}", username);
            throw new UnauthorizedException("User is not authenticated");
        }
        trainerService.activate(id);
    }

    public void deactivateTrainer(String username, String password, Long id) {
        log.info("Deactivating trainer with user name {}", username);
        if (!authenticateTrainee(username, password)) {
            log.warn("Unauthorized access denied for user name {}", username);
            throw new UnauthorizedException("User is not authenticated");
        }
        trainerService.deactivate(id);
    }

    public void changeTrainerPassword(String username, String password, String newPassword) {
        log.info("Changing password trainee with user name {}", username);
        if (!authenticateTrainer(username, password)) {
            log.warn("Unauthorized access denied for user name {}", username);
            throw new UnauthorizedException("User is not authenticated");
        }
        trainerService.changePassword(username, newPassword);
    }

    public List<Training> createTraining(TrainingDTO trainingDTO) {
        log.info("Creating training {}", trainingDTO.toString());
        var training = trainingMapper.toModel(trainingDTO);
        training.setTrainer(trainerService.findById(trainingDTO.getTrainerId()));

        for (Long traineeId : trainingDTO.getTraineeIds()) {
            var trainee = traineeService.findById(traineeId);
            training.setTrainee(trainee);
            trainingService.save(training);
        }

        return trainingService.getTrainingByTrainingTypeName(training.getTrainingType().getTrainingTypeName().name());
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
