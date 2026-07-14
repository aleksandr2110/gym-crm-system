package epam.repository;

import epam.config.TestConfig;
import epam.domain.*;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.DriverManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
@Transactional
public class TrainingRepositoryTest {

    private TrainerRepository trainerRepository;
    private TrainingTypeRepository trainingTypeRepository;
    private TrainingRepository trainingRepository;
    private TraineeRepository traineeRepository;
    private static final String DB_URL = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = ""; // password
    private static final String CHANGELOG_FILE = "config/liquibase/db-changelog-master.xml";

    @BeforeAll
    static void setupDatabase() throws Exception {
        Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        Database database = DatabaseFactory.getInstance()
                .findCorrectDatabaseImplementation(new JdbcConnection(connection));

        try (Liquibase liquibase = new Liquibase(CHANGELOG_FILE, new ClassLoaderResourceAccessor(), database)) {
            liquibase.update(new Contexts(), new LabelExpression());
        }

        System.out.println("Liquibase migrations applied successfully!");
    }

    @Autowired
    public void setTraineeRepository(TrainingRepository trainingRepository,
                                     TraineeRepository traineeRepository,
                                     TrainingTypeRepository trainingTypeRepository,
                                     TrainerRepository trainerRepository) {
        this.trainingRepository = trainingRepository;
        this.traineeRepository = traineeRepository;
        this.trainingTypeRepository = trainingTypeRepository;
        this.trainerRepository = trainerRepository;
    }

    @Test
    void testAddTraining() {
        var trainee = new Trainee();
        trainee.setFirstName("Sergey");
        trainee.setLastName("Vashinskiy");
        trainee.setUserName("Sergey.Vashinskiy");
        trainee.setPassword("fdbcxew3g");
        trainee.setDateOfBirth(LocalDate.of(1989, 10, 13));
        trainee.setAddress("9 sea St");
        trainee.setIsActive(true);
        Trainee createdTrainee = traineeRepository.save(trainee);

        var trainingTypeJavaScript = new TrainingType();
        trainingTypeJavaScript.setTrainingTypeName(TrainingTypeName.valueOf("JAVASCRIPT"));
        trainingTypeRepository.saveTrainingType(List.of(trainingTypeJavaScript));

        var trainer = new Trainer();
        trainer.setFirstName("David");
        trainer.setLastName("Gosling");
        trainer.setUserName("David.Gosling");
        trainer.setPassword("fdbcxew789");
        trainer.setSpecialization(trainingTypeJavaScript);
        trainer.setIsActive(true);
        Trainer createdTrainer = trainerRepository.save(trainer);

        var trainingRequest = new Training();
        trainingRequest.setTrainer(createdTrainer);
        trainingRequest.setTrainee(createdTrainee);
        trainingRequest.setTrainingName("Learning Javascript");
        trainingRequest.setTrainingType(trainingTypeJavaScript);
        trainingRequest.setTrainingDate(LocalDateTime.of(2026, Month.MAY, 11, 12, 15, 00));
        trainingRequest.setTrainingDuration(60);

        trainingRepository.save(trainingRequest);

        List<Training> trainings = trainingRepository.getTrainingByTrainingTypeName(
                TrainingTypeName.getByName("JAVASCRIPT").name());

        assertEquals("Learning Javascript", trainings.get(0).getTrainingName());
        assertEquals("Javascript", trainings.get(0).getTrainingType().getTrainingTypeName().getName());
        assertEquals(60, trainings.get(0).getTrainingDuration());
    }

    @Test
    void testFindTrainingByTrainingTypeName() {
        var trainee = new Trainee();
        trainee.setFirstName("Olga");
        trainee.setLastName("Voronovskaya");
        trainee.setUserName("Olga.Voronovskaya");
        trainee.setPassword("fdbcxew3g");
        trainee.setDateOfBirth(LocalDate.of(1990, 10, 17));
        trainee.setAddress("19 sea St");
        trainee.setIsActive(true);
        Trainee createdTrainee = traineeRepository.save(trainee);

        var trainingTypeTypescript = new TrainingType();
        trainingTypeTypescript.setTrainingTypeName(TrainingTypeName.valueOf("TYPESCRIPT"));
        trainingTypeRepository.saveTrainingType(List.of(trainingTypeTypescript));

        var trainer = new Trainer();
        trainer.setFirstName("Davidson");
        trainer.setLastName("Gosling");
        trainer.setUserName("Davidson.Gosling");
        trainer.setPassword("fdbcxew3ewg");
        trainer.setSpecialization(trainingTypeTypescript);
        trainer.setIsActive(true);
        Trainer createdTrainer = trainerRepository.save(trainer);

        var trainingRequest = new Training();
        trainingRequest.setTrainer(createdTrainer);
        trainingRequest.setTrainee(createdTrainee);
        trainingRequest.setTrainingName("Learning TypeScript");
        trainingRequest.setTrainingType(trainingTypeTypescript);
        trainingRequest.setTrainingDate(LocalDateTime.of(2026, 6, 12, 12, 15, 00));
        trainingRequest.setTrainingDuration(60);

        trainingRepository.save(trainingRequest);

        List<Training> trainings = trainingRepository.findTraineeTrainingsByUserNameAndDate(createdTrainee.getUserName(),
                LocalDateTime.of(2026, 6, 10, 12, 15, 00),
                LocalDateTime.of(2026, 6, 15, 12, 15, 00),
                "TYPESCRIPT");

        assertEquals("Olga.Voronovskaya", trainings.get(0).getTrainee().getUserName());
        assertEquals("Learning TypeScript", trainings.get(0).getTrainingName());
    }
}
