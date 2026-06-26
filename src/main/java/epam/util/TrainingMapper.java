package epam.util;

import epam.dao.TrainingDao;
import epam.domain.Training;
import org.springframework.stereotype.Component;

@Component
public class TrainingMapper {

    public TrainingDao toDao(Training training) {
        TrainingDao trainingDao = new TrainingDao();
        trainingDao.setId(training.getId());
        trainingDao.setTrainer(training.getTrainer());
        trainingDao.setTrainers(training.getTrainers());
        trainingDao.setTrainingName(training.getTrainingName());
        trainingDao.setTrainingType(training.getTrainingType());
        trainingDao.setTrainingDate(training.getTrainingDate());
        trainingDao.setTrainingDuration(training.getTrainingDuration());

        return trainingDao;
    }

    public Training toModel(TrainingDao trainingDao) {
        Training training = new Training();
        training.setId(trainingDao.getId());
        training.setTrainer(trainingDao.getTrainer());
        training.setTrainingName(trainingDao.getTrainingName());
        training.setTrainers(trainingDao.getTrainers());
        training.setTrainingType(trainingDao.getTrainingType());
        training.setTrainingDate(trainingDao.getTrainingDate());
        training.setTrainingDuration(trainingDao.getTrainingDuration());

        return training;
    }
}
