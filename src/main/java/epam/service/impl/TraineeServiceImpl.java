package epam.service.impl;

import epam.annotation.ExecutionTime;
import epam.domain.Trainee;
import epam.exception.UnauthorizedException;
import epam.repository.TraineeRepository;
import epam.service.TraineeService;
import epam.util.UsernameAndPasswordGenerator;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class TraineeServiceImpl implements TraineeService {

    private final TraineeRepository traineeRepository;

    @Autowired
    public TraineeServiceImpl(TraineeRepository traineeRepository) {
        this.traineeRepository = traineeRepository;
    }

    @Transactional
    @Override
    public Trainee save(Trainee trainee) {
        if (trainee.getUserName() == null) {
            setUsername(trainee);
        } else {
            throw new IllegalArgumentException("Attempt to save trainee with username: " + trainee.getUserName());
        }

        return traineeRepository.save(trainee);
    }

    @Override
    @ExecutionTime
    public Trainee findById(Long id) {
        Trainee trainee = traineeRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Trainee not found with id: " + id));

        return trainee;
    }

    @Override
    public Trainee findByUsername(String userName) {
        return traineeRepository.findByUsername(userName).orElseThrow(
                () -> new IllegalArgumentException("Trainee not found with username: " + userName));
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
        Trainee currentTrainee = traineeRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("Trainee not found with userId: " + userId)
        );

        currentTrainee.setFirstName(trainee.getFirstName());
        currentTrainee.setLastName(trainee.getLastName());
        currentTrainee.setUserName(UsernameAndPasswordGenerator.createUsername(
                trainee.getFirstName(),
                trainee.getLastName()));
        currentTrainee.setAddress(trainee.getAddress());
        currentTrainee.setDateOfBirth(trainee.getDateOfBirth());
        currentTrainee.setIsActive(trainee.getIsActive());
        currentTrainee.setTrainers(trainee.getTrainers());
        currentTrainee.setTrainings(trainee.getTrainings());

        return traineeRepository.save(currentTrainee);
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
    public Trainee authenticateTrainee(String username, String password) {
        var entity = traineeRepository.findByUsername(username).orElseThrow(()
                -> new IllegalArgumentException("Trainee not found with username: " + username));
        if (!entity.getPassword().equals(password)) {
            throw new UnauthorizedException("User is not authenticated: " + username);
        }
        return entity;
    }

    @Transactional
    @Override
    public void deleteProfile(String username) {
        traineeRepository.delete(username);
    }

    private void setUsername(Trainee trainee) {
        trainee.setUserName(UsernameAndPasswordGenerator.createUsername(
                trainee.getFirstName(),
                trainee.getLastName()));
        trainee.setPassword(UsernameAndPasswordGenerator.generatePassword());
        List<String> usernameDuplicates = traineeRepository.findUsernamesLike(trainee.getFirstName() + "%");
        trainee.setUserName(trainee.getUserName() + (usernameDuplicates.size() == 0 ? "" : usernameDuplicates.size()));
    }

}
