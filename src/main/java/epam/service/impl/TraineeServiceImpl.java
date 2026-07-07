package epam.service.impl;

import epam.annotation.ExecutionTime;
import epam.domain.Trainee;
import epam.repository.TraineeRepository;
import epam.service.TraineeService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class TraineeServiceImpl implements TraineeService {

    private final TraineeRepository traineeRepository;

    @Autowired
    public TraineeServiceImpl(TraineeRepository traineeRepository) {
        this.traineeRepository = traineeRepository;
    }

    @Transactional
    @Override
    @ExecutionTime
    public Trainee save(Trainee trainee) {

        return traineeRepository.save(trainee);
    }

    @Override
    @ExecutionTime
    public Trainee findById(Long id) {
        Trainee trainee = traineeRepository.findById(id);
        if (trainee == null) {
            throw new NoSuchElementException("User with id: " + id + " not found!");
        }

        return trainee;
    }

    @Override
    public Trainee findByUsername(String userName) {
        return traineeRepository.findByUsername(userName);
    }

    @Override
    public void changePassword(Long id, String newPassword) {
        traineeRepository.changePassword(id, newPassword);
    }

    @Transactional
    @Override
    public void changePassword(String username, String newPassword) {
        traineeRepository.changePassword(username, newPassword);
    }

    @Transactional
    @Override
    public Trainee updateProfile(Trainee trainee, Long userId) {
        Trainee currentTrainee = traineeRepository.findById(userId);
        if (currentTrainee == null) {
            throw new IllegalArgumentException("User with id: " + userId + " not found!");
        }

        currentTrainee.setAddress(trainee.getAddress());
        currentTrainee.setDateOfBirth(trainee.getDateOfBirth());
        currentTrainee.setIsActive(trainee.getIsActive());
        currentTrainee.setTrainers(trainee.getTrainers());
        currentTrainee.setTrainings(trainee.getTrainings());

        return traineeRepository.updateProfile(currentTrainee);
    }

    @Transactional
    @Override
    public void activate(Long id) {
        traineeRepository.activate(id);
    }

    @Transactional
    @Override
    public void deactivate(Long id) {
        traineeRepository.deactivate(id);
    }

    @Override
    public boolean authenticateTrainee(String username, String password) {
        return traineeRepository.authenticate(username, password);
    }

    @Transactional
    @Override
    @ExecutionTime
    public void deleteProfile(String username) {
        traineeRepository.delete(username);
    }
}
