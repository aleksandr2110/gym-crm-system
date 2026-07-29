package epam.application;

import epam.domain.dto.request.*;
import epam.domain.dto.response.*;
import epam.domain.entity.*;
import epam.service.TraineeService;
import epam.service.TrainerService;
import epam.service.TrainingService;
import epam.service.TrainingTypeService;
import epam.util.DataMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
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

    public Trainee authenticateTrainee(String username, String password) {
        return traineeService.authenticateTrainee(username, password);
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

    public void loginTrainer(String username, String password) {

        trainerService.authenticateTrainer(username, password);
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

    public TrainerProfileDTO getTrainerByUsername(String username, String headerUsername, String headerPassword) {
        trainerService.authenticateTrainer(headerUsername, headerPassword);
        var trainer = trainerService.findByUsername(username);

        var trainerProfileDTO = dataMapper.toProfileTrainerDTO(trainer);
        trainerProfileDTO.setIsActive(trainer.isActive());
        trainerProfileDTO.setSpecialization(trainer.getSpecialization().getTrainingTypeName().getName());
        return trainerProfileDTO;
    }

    public TrainerProfileDTO updateTrainerProfile(UpdateTrainerRequestDTO request, String headerUsername, String headerPassword) {
        trainerService.authenticateTrainer(headerUsername, headerPassword);
        var trainer = trainerService.updateProfile(request);

        var trainerProfileDTO = dataMapper.toProfileTrainerDTO(trainer);
        trainerProfileDTO.setSpecialization(trainer.getSpecialization().getTrainingTypeName().getName());
        trainerProfileDTO.setIsActive(trainer.isActive());

        return trainerProfileDTO;
    }

    public void changeTrainerPassword(ChangePasswordRequestDTO request, String headerUsername,
                                      String headerPassword) {
        var trainer = trainerService.authenticateTrainer(headerUsername, headerPassword);
        trainerService.changePassword(request.getUsername(), request.getOldPassword(), request.getNewPassword());
    }

    public void activateDeactivateTrainer(String username, Boolean isActive,
                                          String headerUsername, String headerPassword) {
        trainerService.authenticateTrainer(headerUsername, headerPassword);
        trainerService.activateDeactivateTrainee(username, isActive);
    }

    public Trainer authenticateTrainer(String username, String password) {
        return trainerService.authenticateTrainer(username, password);
    }

    public List<TrainingTypeDTO> getTrainingTypes() {
        var trainingTypes = trainingTypeService.findAll();
        List<TrainingTypeDTO> types = trainingTypes.stream()
                .map(trainingType -> {
                    var trainingTypeDTO = new TrainingTypeDTO();
                    trainingTypeDTO.setId(trainingType.getId());
                    trainingTypeDTO.setTrainingTypeName(trainingType.getTrainingTypeName().name());
                    return trainingTypeDTO;
                }).toList();
        return types;
    }

    public void createTraining(TrainingRequestDTO trainingRequest, String headerUsername, String headerPassword) {
        trainerService.authenticateTrainer(headerUsername, headerPassword);

        var training = dataMapper.toTraining(trainingRequest);
        training.setTrainingDate(LocalDateTime.parse(trainingRequest.getTrainingDate()));
        trainingService.save(training);
    }

    public List<TrainingDTO> getTraineeTraining(TraineeTrainingsRequestDTO filterRequest, String headerUsername,
                                                String headerPassword) {

        trainerService.authenticateTrainer(headerUsername, headerPassword);
        List<Training> trainingsList = trainingService.selectTraineeTrainings(filterRequest);
        List<TrainingDTO> trainingDTOs = trainingsList.stream().map(
                training -> {
                    var trainingResponse = new TrainingDTO();
                    trainingResponse.setTrainingName(training.getTrainingName());
                    trainingResponse.setTrainingType(training.getTrainingType().getTrainingTypeName().getName());
                    trainingResponse.setTrainingDate(training.getTrainingDate());
                    trainingResponse.setTrainingDuration(training.getTrainingDuration());
                    trainingResponse.setTrainerName(training.getTrainer().getUsername());
                    return trainingResponse;
                }
        ).toList();

        return trainingDTOs;
    }
    /*
    public Training findById(Long id) {
        log.info("Find training by id {}", id);
        return trainingService.findTrainingById(id);
    }

    public List<Training> getTrainingByTrainingTypeName(String trainingTypeName) {
        log.info("Find training by training type name {}", trainingTypeName);
        return trainingService.getTrainingByTrainingTypeName(trainingTypeName);
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
