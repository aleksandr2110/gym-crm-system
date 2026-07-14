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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    public Trainee updateTrainee(TraineeDTO traineeDTO, String username, String password) {
        log.info("Updating trainee with username {}", username);
        var trainee = authenticateTrainee(username, password);
        var traineeRequest = traineeMapper.toModel(traineeDTO);
        return traineeService.updateProfile(traineeRequest, trainee.getId());
    }

    public Trainee getTrainee(String username, String password) {
        var trainee = authenticateTrainee(username, password);
        log.info("Getting trainee with userId {}", trainee.getId());
        return traineeService.findById(trainee.getId());
    }

    public void deleteTrainee(String username, String password) {
        log.info("Deleting trainee with user name {}", username);
        var trainee = authenticateTrainee(username, password);
        traineeService.deleteProfile(trainee.getUserName());
    }

    public void activateTrainee(String username, String password) {
        log.info("Activating trainee with user name {}", username);
        var trainee = authenticateTrainee(username, password);
        traineeService.activate(trainee.getId());
    }

    public void deactivateTrainee(String username, String password, Long id) {
        log.info("Deactivating trainee with user name {}", username);
        var trainee = authenticateTrainee(username, password);
        traineeService.deactivate(trainee.getId());
    }

    public void changeTraineePassword(String username, String password, String newPassword) {
        log.info("Changing password trainee's with user name {}", username);
        var trainee = authenticateTrainee(username, password);
        traineeService.changePassword(trainee.getUserName(), newPassword);
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

    public Trainee authenticateTrainee(String username, String password) {
        log.info("Authenticating trainee {}", username);
        return traineeService.authenticateTrainee(username, password);
    }

    public Trainer authenticateTrainer(String username, String password) {
        log.info("Authenticating trainer {}", username);
        return trainerService.authenticateTrainer(username, password);
    }

    public Trainer getTrainer(String username, String password, Long userId) {
        log.info("Getting trainer with userId " + userId);
        var trainer = authenticateTrainer(username, password);
        return trainerService.findById(trainer.getId());
    }

    public Trainer updateTrainer(String username, String password, TrainerDTO trainerDto, Long userId) {
        log.info("Updating trainer with id " + userId);
        var trainer = authenticateTrainer(username, password);
        var trainerRequest =  trainerMapper.toModel(trainerDto);
        return trainerService.updateProfile(trainerRequest, trainer.getId());
    }

    public void activateTrainer(String username, String password) {
        log.info("Activating trainer with user name {}", username);
        var trainer = authenticateTrainer(username, password);
        trainerService.activate(trainer.getId());
    }

    public void deactivateTrainer(String username, String password) {
        log.info("Deactivating trainer with user name {}", username);
        var trainer = authenticateTrainer(username, password);
        trainerService.deactivate(trainer.getId());
    }

    public void changeTrainerPassword(String username, String password, String newPassword) {
        log.info("Changing password trainee with user name {}", username);
        var trainer = authenticateTrainer(username, password);
        trainerService.changePassword(trainer.getUserName(), newPassword);
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

    public List<Training> getTrainingByTrainingTypeName(String trainingTypeName) {
        log.info("Find training by training type name {}", trainingTypeName);
        return trainingService.getTrainingByTrainingTypeName(trainingTypeName);
    }

    public List<Training> getTraineeTrainingByUserNameDateAndTrainingType(String traineeUsername, LocalDateTime fromDate,
                                                                          LocalDateTime toDate, String trainingType) {
        log.info("Getting trainings for trainee {} from {} to {}", traineeUsername, fromDate, toDate);
        return trainingService.selectTraineeTrainings(traineeUsername, fromDate, toDate, trainingType);
    }

    public List<Training> getTrainerTrainingsByUserNameDateAndTrainingType(String trainerUsername, LocalDateTime fromDate,
                                                                           LocalDateTime toDate) {
        log.info("Getting trainings for trainer {} from {} to {}", trainerUsername, fromDate, toDate);
        return trainingService.selectTrainerTrainings(trainerUsername, fromDate, toDate);
    }

    public List<Trainer> getTrainersNotAssignedToTrainee(String traineeUsername) {
        log.info("Getting trainers not assigned to trainee {}", traineeUsername);
        return trainerService.findAllNotAssignedToTrainee(traineeUsername);
    }
}
