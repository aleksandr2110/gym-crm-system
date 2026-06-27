package epam.repository;

import epam.dao.TraineeDao;
import epam.domain.Trainee;
import epam.util.TraineeMapper;
import epam.util.UsernameAndPasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.logging.Logger;

@Repository
public class TraineeRepository implements EntityRepository<Trainee, Long> {

    private final Map<Long, TraineeDao> traineeStorage;
    private final TraineeMapper traineeMapper;
    private static final Logger logger = Logger.getLogger(TraineeRepository.class.getName());

    @Autowired
    public TraineeRepository(@Lazy Map<Long, TraineeDao> traineeStorage, TraineeMapper traineeMapper) {
        this.traineeStorage = traineeStorage;
        this.traineeMapper = traineeMapper;
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
            Integer id = traineeStorage.size();
            long newId = (long) id++;
            trainee.setUserId(appointId((long) ++id));
            ;
        }

        traineeStorage.put(trainee.getUserId(), traineeMapper.toDao(trainee));
        return trainee;
    }

    @Override
    public Trainee select(Long id) {

        if (id == null) {
            logger.warning("Attempt to select user with null id");
            throw new IllegalArgumentException("Attempt to select user with null id");
        }

        TraineeDao selectedTraineeDao = null; // optional
        for (Map.Entry<Long, TraineeDao> entry : traineeStorage.entrySet()) {
            var traineeDao = entry.getValue();
            if (traineeDao.getUserId().longValue() == id.longValue()) {
                selectedTraineeDao = traineeDao;
            }
        }

        return traineeMapper.toModelTrainee(selectedTraineeDao);
    }

    @Override
    public Trainee update(Trainee trainee) {

        Trainee updatedTrainee = null;
        for (Map.Entry<Long, TraineeDao> entry : traineeStorage.entrySet()) {
            var traineeDao = entry.getValue();
            if (traineeDao.getUserId().equals(trainee.getUserId())) {
                TraineeDao updatedDao = traineeMapper.toDao(trainee);
                traineeStorage.put(trainee.getUserId(), updatedDao);
                updatedTrainee = traineeMapper.toModelTrainee(updatedDao);
                break;
            }
        }
        return updatedTrainee;
    }

    @Override
    public void delete(Long id) {
        if (id == null) {
            logger.warning("Attempt to delete user with null id");
            throw new IllegalArgumentException("Username can not be null!" );
        }

        TraineeDao selectedTraineeDao = null;
        for (Map.Entry<Long, TraineeDao> entry : traineeStorage.entrySet()) {
            var traineeDao = entry.getValue();
            if (traineeDao.getUserId().equals(id)) {
                selectedTraineeDao = traineeDao;
            }
        }

        traineeStorage.remove(selectedTraineeDao.getUsername());
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

        for (Map.Entry<Long, TraineeDao> entry : traineeStorage.entrySet()) {
            TraineeDao traineeDao = entry.getValue();
            if (traineeDao.getUserId().longValue() == userId.longValue()) {
                appointId(++userId);
                break;
            }
        }

        return userId;
    }
}
