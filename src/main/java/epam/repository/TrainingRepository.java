package epam.repository;

import epam.dao.InnerDataTrainingDao;
import epam.dao.TrainingDao;
import epam.domain.InnerDataTraining;
import epam.domain.Training;
import epam.util.TrainingMapper;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

@Repository
public class TrainingRepository implements EntityRepository<Training, InnerDataTraining> {

    private final Map<InnerDataTrainingDao, TrainingDao> trainingStorage;
    private final TrainingMapper trainerMapper;
    private static final Logger logger = Logger.getLogger(TrainingRepository.class.getName());

    public TrainingRepository(Map<InnerDataTrainingDao, TrainingDao> trainingStorage,
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

        validate(entity);
        TrainingDao trainingDao = trainerMapper.toDao(entity);
        trainingStorage.put(trainingDao.getInnerDataTraining(), trainingDao);
        return entity;
    }

    @Override
    public Training select(InnerDataTraining innerDataTraining) {
        if (innerDataTraining == null) {
            logger.warning("Attempt to select training with null id");
            throw new IllegalArgumentException("Attempt to select training with null id");
        }

        InnerDataTrainingDao daoId = new InnerDataTrainingDao();
        daoId.setTraineeId(innerDataTraining.getTraineeId());
        daoId.setTrainerId(innerDataTraining.getTrainerId());
        daoId.setTrainingName(innerDataTraining.getTrainingName());

        TrainingDao trainingDao = trainingStorage.get(daoId);
        if (trainingDao == null) {
            logger.warning("Training with id " + innerDataTraining.toString() + " not found");
            throw new NoSuchElementException("Training with id " + innerDataTraining + " not found");
        }

        return trainerMapper.toModel(trainingDao);
    }

    private void validate(Training training) {
        if (training.getInnerDataTraining() == null) {
            logger.warning("Training ID cannot be null");
            throw new IllegalArgumentException("Training ID cannot be null");
        }

        if (training.getInnerDataTraining().getTraineeId() == null) {
            logger.warning("Trainee ID cannot be null");
            throw new IllegalArgumentException("Trainee ID cannot be null");
        }

        if (training.getInnerDataTraining().getTrainerId() == null) {
            logger.warning("Trainer ID cannot be null");
            throw new IllegalArgumentException("Trainer ID cannot be null");
        }

        if (training.getInnerDataTraining().getTrainingName() == null) {
            logger.warning("Training name cannot be null");
            throw new IllegalArgumentException("Training name cannot be null");
        }
    }
}
