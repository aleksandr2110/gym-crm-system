package epam.util;

import epam.dao.InnerDataTrainingDao;
import epam.dao.TrainingDao;
import epam.domain.InnerDataTraining;
import epam.domain.Training;
import org.springframework.stereotype.Component;

@Component
public class TrainingMapper {

    public TrainingDao toDao(Training training) {
        TrainingDao trainingDao = new TrainingDao();
        InnerDataTrainingDao innerDataTrainingDao = new InnerDataTrainingDao();
        innerDataTrainingDao.setTrainerId(training.getInnerDataTraining().getTrainerId());
        innerDataTrainingDao.setTraineeId(training.getInnerDataTraining().getTraineeId());
        innerDataTrainingDao.setTrainingName(training.getInnerDataTraining().getTrainingName());

        trainingDao.setInnerDataTraining(innerDataTrainingDao);
        trainingDao.setTrainingType(training.getTrainingType());
        trainingDao.setTrainingDate(training.getTrainingDate());
        trainingDao.setTrainingDuration(training.getTrainingDuration());

        return trainingDao;
    }

    public Training toModel(TrainingDao trainingDao) {
        Training training = new Training();
        InnerDataTraining innerDataTraining = new InnerDataTraining();
        innerDataTraining.setTraineeId(trainingDao.getInnerDataTraining().getTraineeId());
        innerDataTraining.setTrainerId(trainingDao.getInnerDataTraining().getTrainerId());
        innerDataTraining.setTrainingName(innerDataTraining.getTrainingName());

        training.setInnerDataTraining(innerDataTraining);
        training.setTrainingDate(trainingDao.getTrainingDate());
        training.setTrainingType(trainingDao.getTrainingType());
        training.setTrainingDuration(trainingDao.getTrainingDuration());

        return training;
    }
}
