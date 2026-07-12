package epam.repository;

import epam.config.TestConfig;
import epam.domain.Trainer;
import epam.domain.TrainingType;
import epam.domain.TrainingTypeName;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
@Transactional
public class TrainerRepositoryTest {

    private TrainerRepository trainerRepository;
    private TrainingTypeRepository trainingTypeRepository;
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
    public void setTrainerRepository(TrainerRepository trainerRepository,
                                     TrainingTypeRepository trainingTypeRepository) {
        this.trainerRepository = trainerRepository;
        this.trainingTypeRepository = trainingTypeRepository;
    }

    @Test
    void testSaveTrainee() {
        var trainingTypeJava = new TrainingType();
        trainingTypeJava.setTrainingTypeName(TrainingTypeName.valueOf("JAVA"));
        trainingTypeRepository.saveTrainingType(List.of(trainingTypeJava));

        var trainer = new Trainer();
        trainer.setFirstName("David");
        trainer.setLastName("Gosling");
        trainer.setSpecialization(trainingTypeJava);
        trainer.setIsActive(true);
        Trainer createdTrainer = trainerRepository.save(trainer);

        assertEquals("David", createdTrainer.getFirstName());
        assertEquals("Gosling", createdTrainer.getLastName());
    }

    @Test
    void testFindTrainer() {
        var trainingTypeC = new TrainingType();
        trainingTypeC.setTrainingTypeName(TrainingTypeName.valueOf("C"));
        trainingTypeRepository.saveTrainingType(List.of(trainingTypeC));

        var trainer = new Trainer();
        trainer.setFirstName("Shon");
        trainer.setLastName("Rey");
        trainer.setSpecialization(trainingTypeC);
        trainer.setIsActive(true);
        Trainer createdTrainer = trainerRepository.save(trainer);

        createdTrainer = trainerRepository.findById(createdTrainer.getId());

        assertEquals("Shon", createdTrainer.getFirstName());
        assertEquals("Rey", createdTrainer.getLastName());
        assertEquals("C", trainer.getSpecialization().getTrainingTypeName().getName());
    }

    @Test
    void testUpdateTrainer() {
        var trainingTypeAngular = new TrainingType();
        trainingTypeAngular.setTrainingTypeName(TrainingTypeName.valueOf("ANGULAR"));
        trainingTypeRepository.saveTrainingType(List.of(trainingTypeAngular));

        var trainer = new Trainer();
        trainer.setFirstName("Shany");
        trainer.setLastName("Von");
        trainer.setSpecialization(trainingTypeAngular);
        trainer.setIsActive(true);
        Trainer createdTrainer = trainerRepository.save(trainer);
        createdTrainer.setFirstName("Any");

        trainerRepository.updateProfile(createdTrainer);

        assertEquals("Any", createdTrainer.getFirstName());
        assertEquals("Von", createdTrainer.getLastName());
    }

    @Test
    void testActivateTrainer() {
        var trainingTypeReact = new TrainingType();
        trainingTypeReact.setTrainingTypeName(TrainingTypeName.valueOf("REACT"));
        trainingTypeRepository.saveTrainingType(List.of(trainingTypeReact));

        var trainer = new Trainer();
        trainer.setFirstName("Katerina");
        trainer.setLastName("Vinny");
        trainer.setSpecialization(trainingTypeReact);
        trainer.setIsActive(false);

        Trainer createdTrainer = trainerRepository.save(trainer);
        trainerRepository.activate(createdTrainer.getId());
        createdTrainer = trainerRepository.findById(createdTrainer.getId());

        assertEquals("Katerina", createdTrainer.getFirstName());
        assertEquals("Vinny", createdTrainer.getLastName());
        assertTrue(createdTrainer.getIsActive());
    }
}
