package epam.service.impl;

import epam.domain.Trainee;
import epam.repository.TraineeRepository;
import epam.request.TraineeDTO;
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
    public Trainee create(TraineeDTO traineeDTO) {

        Trainee trainee = new Trainee();
        trainee.setFirstName(traineeDTO.getFirstName());
        trainee.setLastName(traineeDTO.getLastName());
        trainee.setActive(true);
        trainee.setDateOfBirth(traineeDTO.getDateOfBirth());
        trainee.setAddress(traineeDTO.getAddress());

        Trainee createdTrainee = traineeRepository.save(trainee);
        logger.info("User created successfully with userId: " +  createdTrainee.getUserId());
        return createdTrainee;
    }

    @Override
    public Trainee update(TraineeDTO traineeDTO, Long userId) {

        Trainee currentTrainee = traineeRepository.select(userId);
        if (currentTrainee == null) {
            logger.warning("User with id " + userId + " not found");
            throw new IllegalArgumentException("User with id: " + userId + " not found!");
        }

        boolean nameChanged = !currentTrainee.getFirstName().equals(traineeDTO.getFirstName()) ||
                !currentTrainee.getLastName().equals(traineeDTO.getLastName());

        if (nameChanged) {
            logger.info("Name changed. Before first/last name " +
                    currentTrainee.getFirstName() + " " + currentTrainee.getLastName() +
                    " After " + traineeDTO.getFirstName() + " " + traineeDTO.getLastName());
        }

        currentTrainee.setFirstName(traineeDTO.getFirstName());
        currentTrainee.setLastName(traineeDTO.getLastName());
        currentTrainee.setAddress(traineeDTO.getAddress());
        currentTrainee.setDateOfBirth(traineeDTO.getDateOfBirth());
        currentTrainee.setActive(traineeDTO.getActive());

        Trainee updatedTrainee = traineeRepository.update(currentTrainee);
        logger.info("User updated successfully with id " + updatedTrainee.getUserId());
        return updatedTrainee;
    }

    @Override
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
    public void delete(Long id) {
        logger.info("Deleting user with userId: " + id);
        traineeRepository.delete(id);
        logger.info("User deleted successfully with userId: " + id);
    }
}
