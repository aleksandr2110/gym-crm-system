package epam.service.impl;

import epam.annotation.ExecutionTime;
import epam.domain.Trainee;
import epam.repository.TraineeRepository;
import epam.service.TraineeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.logging.Logger;

@Service
public class TraineeServiceImpl implements TraineeService {

    private final TraineeRepository traineeRepository;
    private static final Logger logger = Logger.getLogger(TraineeServiceImpl.class.getName());


    @Autowired
    public TraineeServiceImpl(TraineeRepository traineeRepository) {
        this.traineeRepository = traineeRepository;
    }

    @Override
    @ExecutionTime
    public Trainee create(Trainee trainee) {
        Trainee createdTrainee = traineeRepository.save(trainee);
        logger.info("User created successfully with userId: " +  createdTrainee.getUserId());

        return createdTrainee;
    }

    @Override
    @ExecutionTime
    public Trainee update(Trainee trainee, Long userId) {

        Trainee currentTrainee = traineeRepository.select(userId);
        if (currentTrainee == null) {
            logger.warning("User with id " + userId + " not found");
            throw new IllegalArgumentException("User with id: " + userId + " not found!");
        }

        boolean nameChanged = !currentTrainee.getFirstName().equals(trainee.getFirstName()) ||
                !currentTrainee.getLastName().equals(trainee.getLastName());

        if (nameChanged) {
            logger.info("Name changed. Before first/last name " +
                    currentTrainee.getFirstName() + " " + currentTrainee.getLastName() +
                    " After " + trainee.getFirstName() + " " + trainee.getLastName());
        }

        currentTrainee.setFirstName(trainee.getFirstName());
        currentTrainee.setLastName(trainee.getLastName());
        currentTrainee.setAddress(trainee.getAddress());
        currentTrainee.setDateOfBirth(trainee.getDateOfBirth());
        currentTrainee.setActive(trainee.isActive());

        Trainee updatedTrainee = traineeRepository.update(currentTrainee);
        logger.info("User updated successfully with id " + updatedTrainee.getUserId());
        return updatedTrainee;
    }

    @Override
    @ExecutionTime
    public Trainee select(Long id) {
        Trainee trainee = traineeRepository.select(id);

        if (trainee == null) {
            logger.warning("User with id " + id + " not found");
            throw new NoSuchElementException("User with id: " + id + " not found!");
        }

        logger.info("User found with userId: " + id);
        return trainee;
    }

    @Override
    @ExecutionTime
    public void delete(Long id) {
        logger.info("Deleting user with userId: " + id);
        traineeRepository.delete(id);
        logger.info("User deleted successfully with userId: " + id);
    }
}
