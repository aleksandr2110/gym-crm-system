package epam.repository;

import epam.dao.TrainingDao;
import epam.domain.Training;
import epam.util.TrainingMapper;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

@Repository
public class TrainingRepository implements EntityRepository<Training, String> {

    private final Map<Long, TrainingDao> trainingStorage;
    private final TrainingMapper trainerMapper;
    private static final Logger logger = Logger.getLogger(TrainingRepository.class.getName());

    public TrainingRepository(Map<Long, TrainingDao> trainingStorage,
                              TrainingMapper trainerMapper) {
        this.trainingStorage = trainingStorage;
        this.trainerMapper = trainerMapper;
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
        TrainingDao trainingDao = trainerMapper.toDao(entity);
        trainingStorage.put(trainingDao.getId(), trainingDao);
        return entity;
    }

    @Override
    public Training select(Long id) {
        if (id == null) {
            logger.warning("Attempt to select training with null id");
            throw new IllegalArgumentException("Attempt to select training with null id");
        }

        TrainingDao trainingDao = trainingStorage.get(id);
        if (trainingDao == null) {
            logger.warning("Training with id " + id + " not found");
            throw new NoSuchElementException("Training with id " + id + " not found");
        }

        return trainerMapper.toModel(trainingDao);
    }

    private Long appointId(Long trainingId) {

        for (Map.Entry<Long, TrainingDao> entry : trainingStorage.entrySet()) {
            TrainingDao trainingDao = entry.getValue();
            if (trainingDao.getId().longValue() == trainingId.longValue()) {
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
