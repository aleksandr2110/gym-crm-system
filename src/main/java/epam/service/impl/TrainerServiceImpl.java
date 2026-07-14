package epam.service.impl;

import epam.annotation.ExecutionTime;
import epam.domain.Trainee;
import epam.domain.Trainer;
import epam.domain.Training;
import epam.exception.UnauthorizedException;
import epam.repository.TraineeRepository;
import epam.repository.TrainerRepository;
import epam.repository.TrainingRepository;
import epam.service.TrainerService;
import epam.service.TrainingTypeService;
import epam.util.UsernameAndPasswordGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class TrainerServiceImpl implements TrainerService {

    private final TrainingTypeService trainingTypeService;
    private final TrainingRepository trainingRepository;
    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;

    public TrainerServiceImpl(TrainingTypeService trainingTypeService,
                              TrainingRepository trainingRepository,
                              TraineeRepository traineeRepository, TrainerRepository trainerRepository) {
        this.trainingTypeService = trainingTypeService;
        this.trainingRepository = trainingRepository;
        this.traineeRepository = traineeRepository;
        this.trainerRepository = trainerRepository;
    }

    @Override
    @ExecutionTime
    public Trainer save(Trainer trainer) {

        var trainingType = trainingTypeService.findByName(trainer.getSpecialization()
                        .getTrainingTypeName().name());
        trainer.setSpecialization(trainingType);

        List<Trainee> traineeList = new ArrayList<>();
        for (Trainee trainee: trainer.getTrainees()) {
            var savedTrainee = traineeRepository.findById(trainee.getId());
            savedTrainee.ifPresent(traineeList::add);
        }
        trainer.setTrainees(traineeList);

        Set<Training> trainingsList = new LinkedHashSet<>();
        for (Training training: trainer.getTrainings()) {
            var savedTraining = trainingRepository.findTrainingById(training.getId());
            trainingsList.add(savedTraining);
        }
        trainer.setTrainings(trainingsList);

        if (trainer.getUserName() == null) {
            setUsername(trainer);
        } else {
            throw new IllegalArgumentException("Attempt to save trainer with username: " + trainer.getUserName());
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
    public Trainer findByUsername(String userName) {
        return trainerRepository.findByUsername(userName).orElseThrow(
                () -> new IllegalArgumentException("Trainer not found with username: " + userName));
    }

    @Override
    public void changePassword(Long id, String newPassword) {
        trainerRepository.changePassword(id, newPassword);
    }

    @Override
    public void changePassword(String username, String newPassword) {
        trainerRepository.changePassword(username, newPassword);
    }

    @ExecutionTime
    @Override
    public Trainer updateProfile(Trainer trainer, Long userId) {
        Trainer currentTrainer = trainerRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("Trainer not found with userId: " + userId)
        );
        currentTrainer.setFirstName(trainer.getFirstName());
        currentTrainer.setLastName(trainer.getLastName());
        currentTrainer.setUserName(trainer.getUserName());
        currentTrainer.setSpecialization(trainer.getSpecialization());
        currentTrainer.setTrainees(trainer.getTrainees());
        currentTrainer.setPassword(trainer.getPassword());
        currentTrainer.setTrainings(trainer.getTrainings());
        currentTrainer.setIsActive(trainer.getIsActive());

        return trainerRepository.save(currentTrainer);
    }

    @Override
    public void activate(Long id) {
        trainerRepository.activate(id);
    }

    @Override
    public void deactivate(Long id) {
        trainerRepository.deactivate(id);
    }

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
    public List<Trainer> findAllNotAssignedToTrainee(String traineeUsername) {
        Trainee trainee = traineeRepository.findByUsername(traineeUsername).orElseThrow(()
                -> new IllegalArgumentException("Trainer not found with username: " + traineeUsername));
        if (trainee == null) {
            throw new NoSuchElementException("Trainee not found with username: " + traineeUsername);
        }

        return trainerRepository.findAllNotAssignedToTrainee(traineeUsername);
    }

    private void setUsername(Trainer trainer) {
        trainer.setUserName(UsernameAndPasswordGenerator.createUsername(
                trainer.getFirstName(),
                trainer.getLastName()));
        trainer.setPassword(UsernameAndPasswordGenerator.generatePassword());
        List<String> usernameDuplicates = trainerRepository.findUsernamesLike(trainer.getUserName() + "%");
        trainer.setUserName(trainer.getUserName() + (usernameDuplicates.size() == 0 ? "" : usernameDuplicates.size()));
    }

}
