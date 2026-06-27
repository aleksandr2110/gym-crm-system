package epam.repository;

import epam.domain.Training;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

@Repository
public class TrainingRepository implements EntityRepository<Training, Long> {

    private final Map<Long, Training> trainingStorage;
    private static final Logger logger = Logger.getLogger(TrainingRepository.class.getName());

    public TrainingRepository(Map<Long, Training> trainingStorage) {
        this.trainingStorage = trainingStorage;
    }

    @Override
    public Training save(Training entity) {
        if (entity == null) {
            logger.warning("Attempt to save null training");
            throw new IllegalArgumentException("Attempt to save null training");
        }

        Long trainingId = appointId((long) trainingStorage.size() + 1);
        entity.setId(trainingId);
        validate(entity);
        trainingStorage.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Training select(Long id) {
        if (id == null) {
            logger.warning("Attempt to select training with null id");
            throw new IllegalArgumentException("Attempt to select training with null id");
        }

        Training training = trainingStorage.get(id);
        if (training == null) {
            logger.warning("Training with id " + id + " not found");
            throw new NoSuchElementException("Training with id " + id + " not found");
        }

        return training;
    }

    private Long appointId(Long trainingId) {

        for (Map.Entry<Long, Training> entry : trainingStorage.entrySet()) {
            Training training = entry.getValue();
            if (training.getId().longValue() == trainingId.longValue()) {
                appointId(++trainingId);
                break;
            }
        }
        return trainingId;
    }

    private void validate(Training training) {

        if (training.getTrainer() == null) {
            logger.warning("Training ID cannot be null");
            throw new IllegalArgumentException("Training ID cannot be null");
        }

        if (training.getTrainer().getUserId() == null) {
            logger.warning("Trainer ID cannot be null");
            throw new IllegalArgumentException("Trainer ID cannot be null");
        }

        if (training.getTrainers().get(0).getUserId() == null) {
            logger.warning("Trainee ID cannot be null");
            throw new IllegalArgumentException("Trainee ID cannot be null");
        }

    }
}
