package epam.service.impl;

import epam.annotation.ExecutionTime;
import epam.domain.dto.request.TrainerRequestDTO;
import epam.domain.dto.request.UpdateTrainerRequestDTO;
import epam.domain.dto.response.RegistrationResponseDTO;
import epam.domain.dto.response.TrainerInfoDTO;
import epam.domain.dto.response.TrainerProfileDTO;
import epam.domain.entity.*;
import epam.exception.UnauthorizedException;
import epam.repository.TraineeRepository;
import epam.repository.TrainerRepository;
import epam.repository.TrainingRepository;
import epam.service.TrainerService;
import epam.service.TrainingTypeService;
import epam.util.DataMapper;
import epam.util.UsernameAndPasswordGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
public class TrainerServiceImpl implements TrainerService {

    private final TrainingTypeService trainingTypeService;
    private final TrainingRepository trainingRepository;
    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;
    private final DataMapper dataMapper;

    public TrainerServiceImpl(TrainingTypeService trainingTypeService,
                              TrainingRepository trainingRepository,
                              TraineeRepository traineeRepository,
                              TrainerRepository trainerRepository, DataMapper dataMapper) {
        this.trainingTypeService = trainingTypeService;
        this.trainingRepository = trainingRepository;
        this.traineeRepository = traineeRepository;
        this.trainerRepository = trainerRepository;
        this.dataMapper = dataMapper;
    }

    @Override
    @Transactional
    public Trainer save(Trainer trainer) {
        TrainingTypeName specialization = trainer.getSpecialization().getTrainingTypeName();
        var trainingType = trainingTypeService.findByName(specialization.getName().toUpperCase());
        trainer.setSpecialization(trainingType);

        List<Trainee> traineeList = new ArrayList<>();
        for (Trainee trainee: trainer.getTrainees()) {
            var savedTrainee = traineeRepository.findById(trainee.getId());
            savedTrainee.ifPresent(traineeList::add);
        }
        trainer.setTrainees(traineeList);

        List<Training> trainingsList = new ArrayList<>();
        for (Training training: trainer.getTrainings()) {
            var savedTraining = trainingRepository.findTrainingById(training.getId());
            trainingsList.add(savedTraining);
        }
        trainer.setTrainings(trainingsList);

        if (trainer.getUsername() == null) {
            setUsername(trainer);
        } else {
            throw new IllegalArgumentException("Attempt to save trainer with username: "
                    + trainer.getUsername());
        }

        return trainerRepository.save(trainer);
    }

    @Override
    @ExecutionTime
    public Trainer findById(Long id) {
        var trainer = trainerRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Trainer not found with id: " + id));

        return trainer;
    }

    @Override
    @Transactional
    public TrainerProfileDTO findByUsername(String userName) {
        var trainer = trainerRepository.findByUsername(userName).orElseThrow(
                () -> new IllegalArgumentException("Trainer not found with username: " + userName));
        var trainerProfileDTO = dataMapper.toProfileTrainerDTO(trainer);
        trainerProfileDTO.setIsActive(trainer.isActive());
        trainerProfileDTO.setSpecialization(trainer.getSpecialization().getTrainingTypeName().getName());
        return trainerProfileDTO;
    }

    @Override
    @ExecutionTime
    public void changePassword(Long id, String newPassword) {
        trainerRepository.changePassword(id, newPassword);
    }

    @Transactional
    @Override
    public void changePassword(String username, String oldPassword, String newPassword) {
        var trainer = trainerRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("Trainer not found with username: " + username));
        if (!trainer.getPassword().equals(oldPassword)) {
            throw new IllegalArgumentException("Invalid username or password");
        }
        trainerRepository.changePassword(username, newPassword);
    }

    @Transactional
    @Override
    public TrainerProfileDTO updateProfile(UpdateTrainerRequestDTO requestTrainer) {
        Trainer currentTrainer = trainerRepository.findByUsername(requestTrainer.getUsername()).orElseThrow(
                () -> new IllegalArgumentException("Trainer not found with username: " + requestTrainer.getUsername())
        );
        currentTrainer.setUsername(requestTrainer.getUsername());
        currentTrainer.setFirstName(requestTrainer.getFirstName());
        currentTrainer.setLastName(requestTrainer.getLastName());

        var trainingTypeName = TrainingTypeName.getByName(requestTrainer.getSpecialization().toUpperCase());
        var trainingType = trainingTypeService.findByName(trainingTypeName.getName().toUpperCase());
        currentTrainer.setSpecialization(trainingType);
        currentTrainer.setActive(requestTrainer.getIsActive());

        var updatedTrainer = trainerRepository.save(currentTrainer);
        var trainerProfileDTO = dataMapper.toProfileTrainerDTO(updatedTrainer);
        trainerProfileDTO.setSpecialization(updatedTrainer.getSpecialization().getTrainingTypeName().getName());
        trainerProfileDTO.setIsActive(updatedTrainer.isActive());

        return trainerProfileDTO;
    }

    @Transactional
    @Override
    public void activateDeactivateTrainee(String username, boolean isActive) {
        var entity = trainerRepository.findByUsername(username).orElseThrow(()
                -> new IllegalArgumentException("Trainee not found with id: " + username));;

        if (isActive) {
            trainerRepository.activate(entity.getId());
        } else {
            trainerRepository.deactivate(entity.getId());
        }
    }

    @Transactional
    @Override
    public Trainer authenticateTrainer(String username, String password) {
        var entity = trainerRepository.findByUsername(username).orElseThrow(()
                -> new IllegalArgumentException("Trainer not found with username: " + username));
        if (!entity.getPassword().equals(password)) {
            throw new UnauthorizedException("User is not authenticated: " + username);
        }
        return entity;
    }

    @Override
    public void deleteProfile(String username) {
        trainerRepository.delete(username);
    }

    @Override
    public List<Trainer> findAll() {
        return trainerRepository.findAll();
    }

    @Override
    public List<String> findUsernamesLike(String likeUsername) {
        return trainerRepository.findUsernamesLike(likeUsername);
    }

    @Transactional
    @Override
    public List<Trainer> findAllNotAssignedToTrainee(String traineeUsername) {
        Trainee trainee = traineeRepository.findByUsername(traineeUsername).orElseThrow(()
                -> new IllegalArgumentException("Trainer not found with username: " + traineeUsername));
        if (trainee == null) {
            throw new NoSuchElementException("Trainee not found with username: " + traineeUsername);
        }
        return trainerRepository.findAllNotAssignedToTrainee(traineeUsername);
    }

    private void setUsername(Trainer trainer) {
        trainer.setUsername(UsernameAndPasswordGenerator.createUsername(
                trainer.getFirstName(),
                trainer.getLastName()));
        trainer.setPassword(UsernameAndPasswordGenerator.generatePassword());
        List<String> usernameDuplicates = trainerRepository.findUsernamesLike(trainer.getUsername() + "%");
        trainer.setUsername(trainer.getUsername() + (usernameDuplicates.size() == 0 ? "" : usernameDuplicates.size()));
    }

}
