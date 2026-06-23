package epam.util;

import epam.dao.TraineeDao;
import epam.domain.Trainee;
import org.springframework.stereotype.Component;

@Component
public class TraineeMapper {

    public TraineeDao toDao(Trainee trainee) {
        TraineeDao traineeDao = new TraineeDao();
        traineeDao.setUserId(trainee.getUserId());
        traineeDao.setFirstName(trainee.getFirstName());
        traineeDao.setLastName(trainee.getLastName());
        traineeDao.setUsername(trainee.getUserName());
        traineeDao.setActive(trainee.isActive());
        traineeDao.setDateOfBirth(trainee.getDateOfBirth());
        traineeDao.setAddress(trainee.getAddress());
        traineeDao.setPassword(trainee.getPassword());
        return traineeDao;
    }

    public Trainee toModelTrainee(TraineeDao traineeDao) {
        Trainee trainee = new Trainee();
        trainee.setUserId(traineeDao.getUserId());
        trainee.setPassword(traineeDao.getPassword());
        trainee.setActive(traineeDao.getActive());
        trainee.setFirstName(traineeDao.getFirstName());
        trainee.setLastName(traineeDao.getLastName());
        trainee.setUserName(traineeDao.getUsername());
        trainee.setDateOfBirth(traineeDao.getDateOfBirth());
        trainee.setAddress(traineeDao.getAddress());
        return trainee;
    }
}
