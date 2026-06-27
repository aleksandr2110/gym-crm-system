package epam.repository;

import epam.domain.Trainer;
import epam.util.UsernameAndPasswordGenerator;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.logging.Logger;

@Repository
public class TrainerRepository implements EntityRepository<Trainer, Long> {

    private final Map<Long, Trainer> trainerStorage;
    private static final Logger logger = Logger.getLogger(TrainerRepository.class.getName());

    public TrainerRepository(Map<Long, Trainer> trainerStorage) {
        this.trainerStorage = trainerStorage;
    }

    @Override
    public Trainer save(Trainer trainer) {
        if (trainer == null) {
            logger.warning("Attempt to save null user");
            throw new IllegalArgumentException("Attempt to save null user");
        }

        String username = trainer.getUserName();

        if (username == null) {
            trainer.setUserName(UsernameAndPasswordGenerator.createUsername(
                    trainer.getFirstName(),
                    trainer.getLastName()));
            checkEqualsUsername(trainer.getUserName(), trainer);
            trainer.setPassword(UsernameAndPasswordGenerator.generatePassword());
            trainer.setUserId(appointId((long) trainerStorage.size() + 1));
        }

        trainerStorage.put(trainer.getUserId(), trainer);
        return trainer;
    }

    @Override
    public Trainer select(Long id) {
        if (id == null) {
            logger.warning("Attempt to select user with null id");
            throw new IllegalArgumentException("Attempt to select user with null id");
        }

        Trainer selectedTrainer = null;
        for (Map.Entry<Long, Trainer> entry : trainerStorage.entrySet()) {
            var traineeDao = entry.getValue();
            if (traineeDao.getUserId().longValue() == id.longValue()) {
                selectedTrainer = traineeDao;
            }
        }

        return selectedTrainer;
    }

    @Override
    public Trainer update(Trainer entity) {

        return trainerStorage.put(entity.getUserId(), entity);
    }

    private void checkEqualsUsername(String username, Trainer trainer) {
        Integer identifier;

        if (trainerStorage.containsKey(username)) {
            for (identifier = 1; identifier < 100; identifier++) {
                username = username + identifier;
                if (!trainerStorage.containsKey(username)) {
                    trainer.setUserName(username);
                    logger.info("User with username: " + username);
                    break;
                }
            }
        } else {
            trainer.setUserName(username);
            logger.info("User with username: " + username);
        }
    }

    private Long appointId(Long userId) {

        for (Map.Entry<Long, Trainer> entry : trainerStorage.entrySet()) {
            Trainer traineeDao = entry.getValue();
            if (traineeDao.getUserId().longValue() == userId.longValue()) {
                appointId(++userId);
                break;
            }
        }

        return userId;
    }
}
