package epam.repository;


import epam.domain.Trainee;
import epam.util.UsernameAndPasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.logging.Logger;

@Repository
public class TraineeRepository implements EntityRepository<Trainee, Long> {

    private final Map<Long, Trainee> traineeStorage;
    private static final Logger logger = Logger.getLogger(TraineeRepository.class.getName());

    @Autowired
    public TraineeRepository(Map<Long, Trainee> traineeStorage) {
        this.traineeStorage = traineeStorage;
    }

    @Override
    public Trainee save(Trainee trainee) {
        if (trainee == null) {
            logger.warning("Attempt to save null user");
            throw new IllegalArgumentException("Attempt to save null user");
        }
        String username = trainee.getUserName();

        if (username == null) {
            trainee.setUserName(UsernameAndPasswordGenerator.createUsername(
                    trainee.getFirstName(),
                    trainee.getLastName()));
            checkEqualsUsername(trainee.getUserName(), trainee);
            trainee.setPassword(UsernameAndPasswordGenerator.generatePassword());
            trainee.setUserId(appointId((long) traineeStorage.size() + 1));
        }

        traineeStorage.put(trainee.getUserId(), trainee);
        return trainee;
    }

    @Override
    public Trainee select(Long id) {

        if (id == null) {
            logger.warning("Attempt to select user with null id");
            throw new IllegalArgumentException("Attempt to select user with null id");
        }

        Trainee selectedTrainee = null; // optional
        for (Map.Entry<Long, Trainee> entry : traineeStorage.entrySet()) {
            var trainee = entry.getValue();
            if (trainee.getUserId().longValue() == id.longValue()) {
                selectedTrainee = trainee;
            }
        }

        return selectedTrainee;
    }

    @Override
    public Trainee update(Trainee entity) {

        return traineeStorage.put(entity.getUserId(), entity);
    }

    @Override
    public void delete(Long id) {
        if (id == null) {
            logger.warning("Attempt to delete user with null id");
            throw new IllegalArgumentException("Username can not be null!" );
        }

        traineeStorage.remove(id);
    }

    private void checkEqualsUsername(String username, Trainee trainee) {
        Integer identifier;

        if (traineeStorage.containsKey(username)) {
            for (identifier = 1; identifier < 100; identifier++) {
                username = username + identifier;
                if (!traineeStorage.containsKey(username)) {
                    trainee.setUserName(username);
                    logger.info("User with username: " + username);
                    break;
                }
            }
        } else {
            trainee.setUserName(username);
            logger.info("User with username: " + username);
        }
    }

    private Long appointId(Long userId) {

        for (Map.Entry<Long, Trainee> entry : traineeStorage.entrySet()) {
            Trainee trainee = entry.getValue();
            if (trainee.getUserId().longValue() == userId.longValue()) {
                System.out.println("user id " + userId);
                appointId(++userId);
                break;
            }
        }

        return userId;
    }
}
