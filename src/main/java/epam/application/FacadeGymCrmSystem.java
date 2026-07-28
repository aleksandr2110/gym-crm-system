package epam.application;

import epam.domain.dto.request.*;
import epam.domain.dto.response.RegistrationResponseDTO;
import epam.domain.dto.response.TraineeProfileDTO;
import epam.domain.dto.response.TrainerInfoDTO;
import epam.domain.entity.Trainer;
import epam.service.TraineeService;
import epam.service.TrainerService;
import epam.service.TrainingService;
import epam.service.TrainingTypeService;
import epam.util.DataMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FacadeGymCrmSystem {

    private final TrainerService trainerService;
    private final TraineeService traineeService;
    private final TrainingService trainingService;
    private final DataMapper dataMapper;
    private final TrainingTypeService trainingTypeService;

    @Autowired
    public FacadeGymCrmSystem(TrainerService trainerService, TraineeService traineeService,
                              TrainingService trainingService, DataMapper dataMapper,
                              TrainingTypeService trainingTypeService) {
        this.trainerService = trainerService;
        this.traineeService = traineeService;
        this.trainingService = trainingService;
        this.dataMapper = dataMapper;
        this.trainingTypeService = trainingTypeService;
    }

    public RegistrationResponseDTO createTrainee(TraineeRequestDTO request) {
        var trainee = traineeService.save(request);

        RegistrationResponseDTO response = RegistrationResponseDTO.builder()
                .username(trainee.getUsername())
                .password(trainee.getPassword())
                .build();

        return response;
    }

    public TraineeProfileDTO getTraineeByUsername(String username, String headerUsername,
                                                  String headerPassword) {
        traineeService.authenticateTrainee(headerUsername, headerPassword);
        var trainee = traineeService.findByUsername(username);
        List<Trainer> trainers = trainee.getTrainers();
        List<TrainerInfoDTO> trainerDtos = new ArrayList<>();
        for (Trainer trainer : trainers) {
            trainerDtos.add(dataMapper.toTrainerDTO(trainer));
        }
        var traineeDTO = dataMapper.toProfileTraineeDTO(trainee);
        traineeDTO.setTrainers(trainerDtos);
        traineeDTO.setIsActive(trainee.isActive());
        return traineeDTO;
    }

    public void deleteTrainee(String username, String headerUsername,
                              String headerPassword) {
       traineeService.authenticateTrainee(headerUsername, headerPassword);
       traineeService.deleteProfile(username);
    }

    public List<TrainerInfoDTO> getAvailableTrainers(String username, String headerUsername,
                                                     String headerPassword) {
        traineeService.authenticateTrainee(headerUsername, headerPassword);
        List<Trainer> trainers = trainerService.findAllNotAssignedToTrainee(username);
        List<TrainerInfoDTO> trainerDtos = new ArrayList<>();
        for (Trainer trainer : trainers) {
            trainerDtos.add(dataMapper.toTrainerDTO(trainer));
        }
        return trainerDtos;
    }

    public void loginTrainee(String username, String password) {
        traineeService.authenticateTrainee(username, password);
    }

    public TraineeProfileDTO updateTraineeProfile(UpdateTraineeRequestDTO traineeRequestDTO,
                                                  String headerUsername,
                                                  String headerPassword) {
        traineeService.authenticateTrainee(headerUsername, headerPassword);
        return traineeService.updateProfile(traineeRequestDTO);
    }

    public void changeTraineePassword(ChangePasswordRequestDTO request, String headerUsername,
                               String headerPassword) {
        traineeService.authenticateTrainee(headerUsername, headerPassword);
        traineeService.changePassword(request.getUsername(), request.getOldPassword(), request.getNewPassword());
    }

    public void activateDeactivateTrainee(String username, Boolean isActive, String headerUsername,
                                          String headerPassword) {
        traineeService.authenticateTrainee(headerUsername, headerPassword);
        traineeService.activateDeactivateTrainee(username, isActive);
    }

    public List<TrainerInfoDTO> updateTrainersList(UpdateTraineeTrainersRequestDTO request,
                                                   String headerUsername,
                                                   String headerPassword) {
        traineeService.authenticateTrainee(headerUsername, headerPassword);
        List<Trainer> newTrainers = traineeService.updateTrainersList(request.getTraineeUsername(),
                request.getTrainerUsernames());
        List<TrainerInfoDTO> result = newTrainers.stream()
                .map(dataMapper::toTrainerDTO)
                .toList();
        return result;
    }

    public RegistrationResponseDTO createTrainer(TrainerRequestDTO request) {
        var trainer = dataMapper.toTrainer(request);
        var createdTrainer = trainerService.save(trainer);

        RegistrationResponseDTO trainerDTO = RegistrationResponseDTO.builder()
                .username(createdTrainer.getUsername())
                .password(createdTrainer.getPassword())
                .build();
        return trainerDTO;
    }
    /*
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
     */
}
