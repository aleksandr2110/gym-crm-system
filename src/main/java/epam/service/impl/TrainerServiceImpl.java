package epam.service.impl;

import epam.annotation.ExecutionTime;
import epam.domain.Trainee;
import epam.domain.Trainer;
import epam.domain.Training;
import epam.domain.TrainingType;
import epam.repository.TraineeRepository;
import epam.repository.TrainerRepository;
import epam.repository.TrainingRepository;
import epam.repository.TrainingTypeRepository;
import epam.service.TrainerService;
import epam.service.TrainingTypeService;
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
                .toString().toUpperCase());
        trainer.setSpecialization(trainingType);

        List<Trainee> traineeList = new ArrayList<>();
        for (Trainee trainee: trainer.getTrainees()) {
            var savedTrainee = traineeRepository.findById(trainee.getId());
            traineeList.add(savedTrainee);
        }
        trainer.setTrainees(traineeList);

        Set<Training> trainingsList = new LinkedHashSet<>();
        for (Training training: trainer.getTrainings()) {
            var savedTraining = trainingRepository.findTrainingById(training.getId());
            trainingsList.add(savedTraining);
        }
        trainer.setTrainings(trainingsList);

        return trainerRepository.save(trainer);
    }

    @Override
    @ExecutionTime
    public Trainer findById(Long id) {
        Trainer trainer = trainerRepository.findById(id);

        if (trainer == null)  {
            throw new NoSuchElementException("User not found by id " + id);
        }

        return trainer;
    }

    @Override
    public Trainer findByUsername(String userName) {

        return trainerRepository.findByUsername(userName);
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
    public Trainer updateProfile(Trainer trainer) {
        Trainer currentTrainer = trainerRepository.findById(trainer.getId());
        if (currentTrainer == null) {
            throw new IllegalArgumentException("Trainee with id: " + trainer.getId() + " not found!");
        }
        currentTrainer.setFirstName(trainer.getFirstName());
        currentTrainer.setLastName(trainer.getLastName());
        currentTrainer.setUserName(trainer.getUserName());
        currentTrainer.setSpecialization(trainer.getSpecialization());
        currentTrainer.setTrainees(trainer.getTrainees());
        currentTrainer.setPassword(trainer.getPassword());
        currentTrainer.setTrainings(trainer.getTrainings());
        currentTrainer.setIsActive(trainer.getIsActive());
        //currentTrainer.set

        return trainerRepository.updateProfile(trainer);
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
    public boolean authenticate(String username, String password) {
        return trainerRepository.authenticate(username, password);
    }

    @Override
    public void delete(String username) {
        trainerRepository.delete(username);
    }

    @Override
    public List<Trainer> findAllNotAssignedToTrainee(String traineeUsername) {
        Trainee trainee = traineeRepository.findByUsername(traineeUsername);
        if (trainee == null) {
            throw new NoSuchElementException("Trainee not found with username: " + traineeUsername);
        }

        return trainerRepository.findAllNotAssignedToTrainee(traineeUsername);
    }





}
