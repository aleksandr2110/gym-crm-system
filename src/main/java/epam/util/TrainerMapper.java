package epam.util;

import epam.dao.TrainerDao;
import epam.domain.Trainer;
import org.springframework.stereotype.Component;

@Component
public class TrainerMapper {

    public TrainerDao toDao(Trainer trainer) {
        TrainerDao trainerDao = new TrainerDao();
        trainerDao.setUserId(trainer.getUserId());
        trainerDao.setFirstName(trainer.getFirstName());
        trainerDao.setLastName(trainer.getLastName());
        trainerDao.setUsername(trainer.getUserName());
        trainerDao.setSpecialization(trainer.getSpecialization());
        trainerDao.setActive(trainer.isActive());
        trainerDao.setPassword(trainer.getPassword());
        return trainerDao;
    }
    public Trainer toModel(TrainerDao traineeDao) {
        Trainer trainer = new Trainer();
        trainer.setUserId(traineeDao.getUserId());
        trainer.setFirstName(traineeDao.getFirstName());
        trainer.setLastName(traineeDao.getLastName());
        trainer.setUserName(traineeDao.getUsername());
        trainer.setSpecialization(traineeDao.getSpecialization());
        trainer.setActive(traineeDao.getActive());
        trainer.setPassword(traineeDao.getPassword());
        return trainer;
    }
}
