package epam.repository;

import epam.config.TestConfig;
import epam.domain.Trainee;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.Contexts;
import liquibase.LabelExpression;

import java.sql.Connection;
import java.sql.DriverManager;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
@Transactional
public class TraineeRepositoryTest {

    private TraineeRepository traineeRepository;
    private static final String DB_URL = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";
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
    public void setTraineeRepository(TraineeRepository traineeRepository) {
        this.traineeRepository = traineeRepository;
    }

    @Test
    void testSaveTrainee() {
        var trainee = new Trainee();
        trainee.setFirstName("Jonny");
        trainee.setLastName("Dep");
        trainee.setUserName("Jonny.Dep");
        trainee.setPassword("fdgfdgfdg");
        trainee.setDateOfBirth(LocalDate.of(1976, 2, 21));
        trainee.setAddress("3 Bank St");
        trainee.setIsActive(true);

        Trainee createdTrainee = traineeRepository.save(trainee);
        assertNotNull(createdTrainee);
        assertEquals("Jonny", createdTrainee.getFirstName());
        assertEquals("Dep", createdTrainee.getLastName());
    }

    @Test
    void testFindTrainee() {
        var trainee = new Trainee();
        trainee.setFirstName("Jonson");
        trainee.setLastName("Dip");
        trainee.setUserName("Jonson.Dip");
        trainee.setPassword("fdgfdgfd43g");
        trainee.setDateOfBirth(LocalDate.of(1971, 2, 10));
        trainee.setAddress("88 Bank St");
        trainee.setIsActive(true);

        Trainee createdTrainee = traineeRepository.save(trainee);
        var userId = createdTrainee.getId();
        createdTrainee = traineeRepository.findById(createdTrainee.getId()).orElseThrow(
                () -> new IllegalArgumentException("Trainer not found with userId: " +  userId)
        );

        assertNotNull(createdTrainee);
        assertEquals("Jonson", createdTrainee.getFirstName());
        assertEquals("Dip", createdTrainee.getLastName());
    }

    @Test
    void testActivateTrainee() {
        var trainee = new Trainee();
        trainee.setFirstName("Sabrina");
        trainee.setLastName("Karpenter");
        trainee.setUserName("Sabrina.Karpenter");
        trainee.setPassword("fdgfdgfd4ew3g");
        trainee.setDateOfBirth(LocalDate.of(1998, 7, 13));
        trainee.setAddress("71 Sunny St");
        trainee.setIsActive(false);

        Trainee createdTrainee = traineeRepository.save(trainee);
        var userId = createdTrainee.getId();
        traineeRepository.activate(createdTrainee.getId());
        createdTrainee = traineeRepository.findById(createdTrainee.getId()).orElseThrow(
                () -> new IllegalArgumentException("Trainer not found with userId: " +  userId)
        );

        assertEquals("Sabrina", createdTrainee.getFirstName());
        assertEquals("Karpenter", createdTrainee.getLastName());
        assertTrue(createdTrainee.getIsActive());
    }

    @Test
    void testCreateAndDeleteTrainee() {
        var trainee = new Trainee();
        trainee.setFirstName("Sara");
        trainee.setLastName("Connor");
        trainee.setUserName("Sara.Connor");
        trainee.setPassword("fdbcxew3g");
        trainee.setDateOfBirth(LocalDate.of(1978, 2, 13));
        trainee.setAddress("90 sea St");
        trainee.setIsActive(true);

        Trainee createdTrainee = traineeRepository.save(trainee);
        traineeRepository.delete(createdTrainee.getUserName());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            traineeRepository.findById(createdTrainee.getId()).orElseThrow(
                    () -> new IllegalArgumentException("Trainee not found with id: " + createdTrainee.getId())
            );
        });

    }
}
