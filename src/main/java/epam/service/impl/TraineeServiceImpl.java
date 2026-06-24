package epam.service.impl;

import epam.domain.Trainee;
import epam.repository.TraineeRepository;
import epam.request.TraineeRequest;
import epam.service.TraineeService;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.logging.Logger;

@Service
public class TraineeServiceImpl implements TraineeService {

    private final TraineeRepository traineeRepository;
    private static final Logger logger = Logger.getLogger(TraineeServiceImpl.class.getName());

    public TraineeServiceImpl(TraineeRepository traineeRepository) {
        this.traineeRepository = traineeRepository;
    }

    @Override
    public Trainee create(TraineeRequest traineeRequest) {
        logger.info("Creating user: " +  traineeRequest.getFirstName() + " " +
                traineeRequest.getLastName());

        Trainee trainee = new Trainee();
        trainee.setFirstName(traineeRequest.getFirstName());
        trainee.setLastName(traineeRequest.getLastName());
        trainee.setActive(true);
        trainee.setDateOfBirth(traineeRequest.getDateOfBirth());
        trainee.setAddress(traineeRequest.getAddress());

        Trainee createdTrainee = traineeRepository.save(trainee);
        logger.info("User created successfully with userId: " +  createdTrainee.getUserId());
        return createdTrainee;
    }

    @Override
    public Trainee update(TraineeRequest traineeRequest, String userId) {
        logger.info("Updating user with new data: " + traineeRequest.getFirstName() +
                " " + traineeRequest.getLastName());

        Trainee currentTrainee = traineeRepository.select(userId);
        if (currentTrainee == null) {
            logger.warning("User with id " + userId + " not found");
            throw new IllegalArgumentException("User with id: " + userId + " not found!");
        }

        boolean nameChanged = !currentTrainee.getFirstName().equals(traineeRequest.getFirstName()) ||
                !currentTrainee.getLastName().equals(traineeRequest.getLastName());

        if (nameChanged) {
            logger.info("Name changed. Before first/last name " +
                    currentTrainee.getFirstName() + " " + currentTrainee.getLastName() +
                    " After " + traineeRequest.getFirstName() + " " + traineeRequest.getLastName());
        }

        currentTrainee.setFirstName(traineeRequest.getFirstName());
        currentTrainee.setLastName(traineeRequest.getLastName());
        currentTrainee.setAddress(traineeRequest.getAddress());
        currentTrainee.setDateOfBirth(traineeRequest.getDateOfBirth());
        currentTrainee.setActive(traineeRequest.getActive());

        Trainee updatedTrainee = traineeRepository.update(currentTrainee);
        logger.info("User updated successfully with id " + updatedTrainee.getUserId());
        return updatedTrainee;
    }

    @Override
    public Trainee select(String id) {
        logger.info("Selecting user by userId: " +  id);
        Trainee trainee = traineeRepository.select(id);

        if (trainee == null) {
            logger.warning("User with id " + id + " not found");
            throw new NoSuchElementException("User with id: " + id + " not found!");
        }

        logger.info("User found with userId: " + id);
        return trainee;
    }

    @Override
    public void delete(String id) {
        logger.info("Deleting user with userId: " + id);
        traineeRepository.delete(id);
        logger.info("User deleted successfully with userId: " + id);
    }
}
