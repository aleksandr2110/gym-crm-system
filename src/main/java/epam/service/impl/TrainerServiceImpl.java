package epam.service.impl;

import epam.domain.Trainer;
import epam.repository.TrainerRepository;
import epam.request.TrainerDTO;
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
    public Trainer create(TrainerDTO trainerDTO) {
        logger.info("Creating trainer: " + trainerDTO.getFirstName() + " " +
                trainerDTO.getLastName());

        Trainer trainer = new Trainer();
        trainer.setFirstName(trainerDTO.getFirstName());
        trainer.setLastName(trainerDTO.getLastName());
        trainer.setActive(true);
        trainer.setSpecialization(trainerDTO.getSpecialization());

        Trainer createdTrainer = trainerRepository.save(trainer);

        logger.info("Trainer created successfully with id : " + createdTrainer.getUserId());
        return createdTrainer;
    }

    @Override
    public Trainer update(TrainerDTO trainerDTO, Long userId) {

        Trainer currentTrainer = trainerRepository.select(userId);
        if (currentTrainer == null) {
            logger.warning("User with username " + userId + " not found");
            throw new IllegalArgumentException("User with id: " + userId + " not found!");
        }

        boolean nameChanged = !currentTrainer.getFirstName().equals(trainerDTO.getFirstName()) ||
                !currentTrainer.getLastName().equals(trainerDTO.getLastName());

        currentTrainer.setFirstName(trainerDTO.getFirstName());
        currentTrainer.setLastName(trainerDTO.getLastName());
        currentTrainer.setSpecialization(trainerDTO.getSpecialization());
        currentTrainer.setActive(trainerDTO.getActive());

        if (nameChanged) {
            logger.info("Name changed. Before first/last name: " +
                    currentTrainer.getFirstName() + " " + currentTrainer.getLastName() +
                    " After: " + trainerDTO.getFirstName() + " " + trainerDTO.getLastName());
        }

        Trainer updatedTrainer = trainerRepository.update(currentTrainer);
        logger.info("User updated successfully with id {}" + updatedTrainer.getUserId());

        return updatedTrainer;
    }

    @Override
    public Trainer select(Long id) {
        logger.info("Selecting user by username: " +  id);
        Trainer trainer = trainerRepository.select(id);

        if (trainer == null)  {
            throw new NoSuchElementException("User not found by id " + id);
        }
        logger.info("User found with userId: " + id);

        return trainer;
    }

}
