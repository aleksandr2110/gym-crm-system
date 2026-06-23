package epam.repository;

import epam.dao.TrainerDao;
import epam.domain.Trainer;
import epam.util.TrainerMapper;
import epam.util.UsernameAndPasswordGenerator;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.logging.Logger;

@Repository
public class TrainerRepository implements EntityRepository<Trainer, String> {

    private final Map<String, TrainerDao> trainerStorage;
    private final TrainerMapper trainerMapper;
    private static final Logger logger = Logger.getLogger(TrainerRepository.class.getName());

    public TrainerRepository(Map<String, TrainerDao> trainerStorage, TrainerMapper trainerMapper) {
        this.trainerStorage = trainerStorage;
        this.trainerMapper = trainerMapper;
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
            Integer id = trainerStorage.size();
            trainer.setUserId((++id).toString());
            appointId(trainer);
        }

        trainerStorage.put(trainer.getUserName(), trainerMapper.toDao(trainer));
        return trainer;
    }

    @Override
    public Trainer select(String id) {
        if (id == null) {
            logger.warning("Attempt to select user with null id");
            throw new IllegalArgumentException("Attempt to select user with null id");
        }

        TrainerDao selectedTrainerDao = null;
        for (Map.Entry<String, TrainerDao> entry : trainerStorage.entrySet()) {
            var traineeDao = entry.getValue();
            if (traineeDao.getUserId().equals(id)) {
                selectedTrainerDao = traineeDao;
            }
        }

        return trainerMapper.toModel(selectedTrainerDao);
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

    private void appointId(Trainer trainee) {
        Integer newId = Integer.parseInt(trainee.getUserId() + 1);

        for (Map.Entry<String, TrainerDao> entry : trainerStorage.entrySet()) {
            TrainerDao traineeDao = entry.getValue();
            if (traineeDao.getUserId().equals(trainee.getUserId())) {
                trainee.setUserId(newId.toString());
                appointId(trainee);
                break;
            }
        }

        logger.info("Id " + trainee.getUserId() + " assigned for username: " + trainee.getUserName());
    }
}
