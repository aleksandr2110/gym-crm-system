package epam;

import epam.application.FacadeGymCrmSystem;
import epam.config.ApplicationConfig;
import epam.config.DataSilosConfig;
import epam.repository.TraineeRepository;
import epam.repository.TrainerRepository;
import epam.repository.TrainingRepository;
import epam.service.impl.TraineeServiceImpl;
import epam.service.impl.TrainerServiceImpl;
import epam.service.impl.TrainingServiceImpl;
import epam.storage.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ApplicationConfig.class, DataSilosConfig.class})
public class AllBeansTest {

    private ApplicationContext applicationContext;

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Test
    void testAllDaoBeansExist() {
        assertNotNull(applicationContext.getBean(TraineeRepository.class));
        assertNotNull(applicationContext.getBean(TrainerRepository.class));
        assertNotNull(applicationContext.getBean(TrainingRepository.class));
    }

    @Test
    void testAllServiceBeansExist() {
        assertNotNull(applicationContext.getBean(TraineeServiceImpl.class));
        assertNotNull(applicationContext.getBean(TrainerServiceImpl.class));
        assertNotNull(applicationContext.getBean(TrainingServiceImpl.class));
    }

    @Test
    void testFacadeBeanExists() {
        assertNotNull(applicationContext.getBean(FacadeGymCrmSystem.class));
    }

    @Test
    void testStorageBeansExist() {
        assertNotNull(applicationContext.getBean("userStorage"));
        assertNotNull(applicationContext.getBean("traineeStorage"));
        assertNotNull(applicationContext.getBean("trainerStorage"));
        assertNotNull(applicationContext.getBean("trainingStorage"));
        assertNotNull(applicationContext.getBean("trainingTypeStorage"));
    }

    @Test
    void testUtilityBeansExist() {
        assertNotNull(applicationContext.getBean(StorageLoader.class));
    }

    @Test
    void testDataLoaderBeansExist() {
        assertNotNull(applicationContext.getBean(UserDataLoader.class));
        assertNotNull(applicationContext.getBean(TraineeDataLoader.class));
        assertNotNull(applicationContext.getBean(TrainerDataLoader.class));
        assertNotNull(applicationContext.getBean(TrainingDataLoader.class));
        assertNotNull(applicationContext.getBean(TrainingTypeDataLoader.class));
    }
}
