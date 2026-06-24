package epam.service.impl;

import epam.domain.Trainee;
import epam.domain.Trainer;
import epam.repository.TrainerRepository;
import epam.request.TrainerRequest;
import epam.service.TrainerService;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.logging.Logger;

@Service
public class TrainerServiceImpl implements TrainerService {

    private static final Logger logger = Logger.getLogger(TraineeServiceImpl.class.getName());
    private final TrainerRepository trainerRepository;

    public TrainerServiceImpl(TrainerRepository trainerRepository) {
        this.trainerRepository = trainerRepository;
    }

    @Override
    public Trainer create(TrainerRequest trainerRequest) {
        logger.info("Creating trainer: " + trainerRequest.getFirstName() + " " +
                trainerRequest.getLastName());

        Trainer trainer = new Trainer();
        trainer.setFirstName(trainerRequest.getFirstName());
        trainer.setLastName(trainerRequest.getLastName());
        trainer.setActive(true);
        trainer.setSpecialization(trainerRequest.getSpecialization());

        Trainer createdTrainer = trainerRepository.save(trainer);

        logger.info("Trainer created successfully with id : " + createdTrainer.getUserId());
        return createdTrainer;
    }

    @Override
    public Trainer update(TrainerRequest trainerRequest, String userId) {
        logger.info("Updating user: " + trainerRequest.getFirstName() + " " + trainerRequest.getLastName());

        Trainer currentTrainer = trainerRepository.select(userId);
        if (currentTrainer == null) {
            logger.warning("User with username " + userId + " not found");
            throw new IllegalArgumentException("User with id: " + userId + " not found!");
        }

        boolean nameChanged = !currentTrainer.getFirstName().equals(trainerRequest.getFirstName()) ||
                !currentTrainer.getLastName().equals(trainerRequest.getLastName());

        currentTrainer.setFirstName(trainerRequest.getFirstName());
        currentTrainer.setLastName(trainerRequest.getLastName());
        currentTrainer.setSpecialization(trainerRequest.getSpecialization());
        currentTrainer.setActive(trainerRequest.getActive());

        if (nameChanged) {
            logger.info("Name changed. Before first/last name: " +
                    currentTrainer.getFirstName() + " " + currentTrainer.getLastName() +
                    " After: " + trainerRequest.getFirstName() + " " + trainerRequest.getLastName());
        }

        Trainer updatedTrainer = trainerRepository.update(currentTrainer);
        logger.info("User updated successfully with id {}" + updatedTrainer.getUserId());

        return updatedTrainer;
    }

    @Override
    public Trainer select(String id) {
        logger.info("Selecting user by username: " +  id);
        Trainer trainer = trainerRepository.select(id);

        if (trainer == null)  {
            throw new NoSuchElementException("User not found by id " + id);
        }
        logger.info("User found with userId: " + id);

        return trainer;
    }

}
