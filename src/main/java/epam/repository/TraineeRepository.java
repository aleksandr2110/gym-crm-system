package epam.repository;

import epam.dao.TraineeDao;
import epam.domain.Trainee;
import epam.util.TraineeMapper;
import epam.util.UsernameAndPasswordGenerator;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.logging.Logger;

@Repository
public class TraineeRepository implements EntityRepository<Trainee, String> {

    private final Map<String, TraineeDao> traineeStorage;
    private final TraineeMapper traineeMapper;
    private static final Logger logger = Logger.getLogger(TraineeRepository.class.getName());

    public TraineeRepository(Map<String, TraineeDao> traineeStorage, TraineeMapper traineeMapper) {
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
            trainee.setUserId((++id).toString());
            appointId(trainee);
        }

        traineeStorage.put(trainee.getUserName(), traineeMapper.toDao(trainee));
        logger.info("New trainee has been added to the storage");
        return trainee;
    }

    @Override
    public Trainee select(String id) {

        if (id == null) {
            logger.warning("Attempt to select user with null id");
            throw new IllegalArgumentException("Attempt to select user with null id");
        }

        TraineeDao selectedTraineeDao = null;
        for (Map.Entry<String, TraineeDao> entry : traineeStorage.entrySet()) {
            var traineeDao = entry.getValue();
            if (traineeDao.getUserId().equals(id)) {
                selectedTraineeDao = traineeDao;
            }
        }

        return traineeMapper.toModelTrainee(selectedTraineeDao);
    }

    @Override
    public void delete(String id) {
        if (id == null) {
            logger.warning("Attempt to delete user with null id");
            throw new IllegalArgumentException("Username can not be null!" );
        }

        TraineeDao selectedTraineeDao = null;
        for (Map.Entry<String, TraineeDao> entry : traineeStorage.entrySet()) {
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

    private void appointId(Trainee trainee) {
        Integer newId = Integer.parseInt(trainee.getUserId() + 1);

        for (Map.Entry<String, TraineeDao> entry : traineeStorage.entrySet()) {
            TraineeDao traineeDao = entry.getValue();
            if (traineeDao.getUserId().equals(trainee.getUserId())) {
                trainee.setUserId(newId.toString());
                appointId(trainee);
                break;
            }
        }

        logger.info("Id " + trainee.getUserId() + " assigned for username: " + trainee.getUserName());
    }
}
