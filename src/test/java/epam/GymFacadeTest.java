package epam;

import epam.application.FacadeGymCrmSystem;
import epam.config.AppConfig;
import epam.config.DatabaseConfig;
import epam.config.JpaConfigTest;
import epam.config.TestConfig;
import epam.domain.Trainee;
import epam.domain.Trainer;
import epam.domain.TrainingTypeName;
import epam.exception.UnauthorizedException;
import epam.request.TraineeDTO;
import epam.request.TrainerDTO;
import epam.request.TrainingTypeDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class}) // JpaConfigTest , AppConfig.class
@Transactional
public class GymFacadeTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private FacadeGymCrmSystem gymFacade;

    @Test
    void testCreateTrainee() {
        var traineeDTO = new TraineeDTO();
        traineeDTO.setFirstName("Jonny");
        traineeDTO.setLastName("Dep");
        traineeDTO.setDateOfBirth(LocalDate.of(1978, 8, 21));
        traineeDTO.setAddress("3 Bank St");
        traineeDTO.setActive(true);

        Trainee createdTrainee = gymFacade.createTrainee(traineeDTO);

        assertNotNull(createdTrainee);
        assertEquals("Jonny", createdTrainee.getFirstName());
        assertEquals("Dep", createdTrainee.getLastName());
        assertEquals(LocalDate.of(1978, 8, 21), createdTrainee.getDateOfBirth());
        assertEquals("3 Bank St", createdTrainee.getAddress());
        assertNotNull("Jonny.Dep", createdTrainee.getUserName());
    }

    @Test
    void testGetTrainee() {
        var traineeDTO = new TraineeDTO();
        traineeDTO.setFirstName("Jastin");
        traineeDTO.setLastName("Timbarlake");
        traineeDTO.setDateOfBirth(LocalDate.of(1984, 3, 14));
        traineeDTO.setAddress("11/2 Dark Ave");
        traineeDTO.setActive(true);

        var createdTrainee = gymFacade.createTrainee(traineeDTO);
        Long userId = createdTrainee.getId();
        var retrievedTrainee = gymFacade.getTrainee(createdTrainee.getUserName(),
                createdTrainee.getPassword(), userId);

        assertNotNull(retrievedTrainee);
        assertEquals(userId, retrievedTrainee.getId());
        assertEquals("Jastin", retrievedTrainee.getFirstName());
        assertEquals("Timbarlake", retrievedTrainee.getLastName());
        assertEquals(LocalDate.of(1984, 3, 14), retrievedTrainee.getDateOfBirth());
        assertEquals("11/2 Dark Ave", retrievedTrainee.getAddress());
        assertNotNull("Jastin.Timbarlake", retrievedTrainee.getUserName());
    }

    @Test
    void testCreateAndDeleteTrainee() {
        var traineeDTO = new TraineeDTO();
        traineeDTO.setFirstName("Jakarta");
        traineeDTO.setLastName("Timbarlake2");
        traineeDTO.setDateOfBirth(LocalDate.of(1987, 5, 4));
        traineeDTO.setAddress("16 White Ave");
        traineeDTO.setActive(true);

        var createdTrainee = gymFacade.createTrainee(traineeDTO);
        gymFacade.deleteTrainee(createdTrainee.getUserName(), createdTrainee.getPassword());

        Exception exception = assertThrows(UnauthorizedException.class, () -> {
            gymFacade.getTrainee(createdTrainee.getUserName(),
                    createdTrainee.getPassword(), createdTrainee.getId());
        });
    }

    @Test
    void testUpdateTrainee() {
        TraineeDTO traineeDTO = new TraineeDTO();
        traineeDTO.setFirstName("Yil");
        traineeDTO.setLastName("Smith");
        traineeDTO.setDateOfBirth(LocalDate.of(1977, 9, 10));
        traineeDTO.setAddress("22/3 Red St");
        traineeDTO.setActive(true);

        Trainee created = gymFacade.createTrainee(traineeDTO);
        Long userId = created.getId();

        var updateDto = new TraineeDTO();
        updateDto.setFirstName("Yil");
        updateDto.setLastName("Smith");
        updateDto.setDateOfBirth(LocalDate.of(1981, 5, 11));
        updateDto.setAddress("43 Stone St");
        updateDto.setActive(true);

        Trainee updatedTrainee = gymFacade.updateTrainee(updateDto,
                created.getUserName(), created.getPassword(), userId);

        assertNotNull(updatedTrainee);
        assertEquals("Yil", updatedTrainee.getFirstName());
        assertEquals("Smith", updatedTrainee.getLastName());
        assertEquals("43 Stone St", updatedTrainee.getAddress());
        assertEquals(LocalDate.of(1981, 5, 11), updatedTrainee.getDateOfBirth());
        assertEquals("Yil.Smith", updatedTrainee.getUserName());
    }

    @Test
    void testActivateTrainee() {
        TraineeDTO traineeDTO = new TraineeDTO();
        traineeDTO.setFirstName("Mila");
        traineeDTO.setLastName("Smith");
        traineeDTO.setDateOfBirth(LocalDate.of(1989, 3, 30));
        traineeDTO.setAddress("45 Red stone St");
        traineeDTO.setActive(false);

        Trainee createdTrainee = gymFacade.createTrainee(traineeDTO);
        Long userId = createdTrainee.getId();

        gymFacade.activateTrainee(createdTrainee.getUserName(), createdTrainee.getPassword(), userId);
        var retrievedTrainee = gymFacade.getTrainee(createdTrainee.getUserName(),
                createdTrainee.getPassword(), userId);

        assertNotNull(retrievedTrainee);
        assertEquals("Mila", retrievedTrainee.getFirstName());
        assertEquals("Smith", retrievedTrainee.getLastName());
        assertTrue(retrievedTrainee.getIsActive());
    }


    @Test
    void testDeactivateTrainee() {
        TraineeDTO traineeDTO = new TraineeDTO();
        traineeDTO.setFirstName("Maya");
        traineeDTO.setLastName("Salman");
        traineeDTO.setDateOfBirth(LocalDate.of(1989, 3, 30));
        traineeDTO.setAddress("45 Red stone St");
        traineeDTO.setActive(true);

        Trainee createdTrainee = gymFacade.createTrainee(traineeDTO);
        Long userId = createdTrainee.getId();

        gymFacade.deactivateTrainee(createdTrainee.getUserName(), createdTrainee.getPassword(), userId);
        var retrievedTrainee = gymFacade.getTrainee(createdTrainee.getUserName(),
                createdTrainee.getPassword(), userId);

        assertNotNull(retrievedTrainee);
        assertEquals("Maya", retrievedTrainee.getFirstName());
        assertEquals("Salman", retrievedTrainee.getLastName());
        assertFalse(retrievedTrainee.getIsActive());
    }

    @Test
    void testChangePasswordTrainee() {
        TraineeDTO traineeDTO = new TraineeDTO();
        traineeDTO.setFirstName("Richard");
        traineeDTO.setLastName("Hatchinson");
        traineeDTO.setDateOfBirth(LocalDate.of(1983, 5, 23));
        traineeDTO.setAddress("67 Blue can St");
        traineeDTO.setActive(true);

        Trainee createdTrainee = gymFacade.createTrainee(traineeDTO);
        Long userId = createdTrainee.getId();
        String newPassword = "fbgberbrebg";

        gymFacade.changeTraineePassword(createdTrainee.getUserName(), createdTrainee.getPassword(), newPassword);
        var retrievedTrainee = gymFacade.getTrainee(createdTrainee.getUserName(),
                newPassword, userId);

        assertEquals("Richard", retrievedTrainee.getFirstName());
        assertEquals("Hatchinson", retrievedTrainee.getLastName());
        assertEquals(newPassword, retrievedTrainee.getPassword());
    }

    @Test
    void testCreateTrainer() {
        var trainingTypeJava = new TrainingTypeDTO();
        trainingTypeJava.setTrainingTypeName(TrainingTypeName.valueOf("JAVA"));
        var trainingTypeJavaScript = new TrainingTypeDTO();
        trainingTypeJavaScript.setTrainingTypeName(TrainingTypeName.valueOf("JAVASCRIPT"));
        gymFacade.createTrainingType(List.of(trainingTypeJava, trainingTypeJavaScript));

        var trainerDto = new TrainerDTO();
        trainerDto.setFirstName("Evgeniy");
        trainerDto.setLastName("Syleimanov");
        trainerDto.setSpecialization("Java");
        trainerDto.setActive(true);

        Trainer createdTrainer = gymFacade.createTrainer(trainerDto);

        assertNotNull(createdTrainer);
        //assertEquals("JAVA", createdTrainer.getSpecialization().getTrainingTypeName());
        assertEquals("Evgeniy", createdTrainer.getFirstName());
        assertEquals("Syleimanov", createdTrainer.getLastName());
    }

    @Test
    void testGetTrainer() {
        TrainerDTO trainerDto = new TrainerDTO();
        trainerDto.setFirstName("David");
        trainerDto.setLastName("Gosling");
        trainerDto.setSpecialization("Java");
        trainerDto.setActive(true);

        Trainer created = gymFacade.createTrainer(trainerDto);
        Long userId = created.getId();

        Trainer retrievedTrainer = gymFacade.getTrainer(userId);

        assertNotNull(retrievedTrainer);
        assertEquals(userId, retrievedTrainer.getId());
        assertEquals("Java", retrievedTrainer.getSpecialization());
    }

    /*@Test
    void testUpdateTrainer() {
        var trainerRequest = new TrainerDTO();
        trainerRequest.setFirstName("Alexey");
        trainerRequest.setLastName("Litovchenko");
        trainerRequest.setSpecialization("C++");
        trainerRequest.setActive(true);

        Trainer created = gymFacade.createTrainer(trainerRequest);
        Long userId = created.getId();

        var updateRequest = new TrainerDTO();
        updateRequest.setFirstName("Jeremy");
        updateRequest.setLastName("Man");
        updateRequest.setSpecialization("Python");
        updateRequest.setActive(true);

        Trainer updated = gymFacade.updateTrainer(updateRequest, userId);

        assertNotNull(updated);
        assertEquals("Jeremy", updated.getFirstName());
        assertEquals("Man", updated.getLastName());
        assertEquals("Python", updated.getSpecialization());
    }

    @Test
    void testCreateTraining() {
        var traineeRequest = new TraineeDTO();
        traineeRequest.setFirstName("Sabrina");
        traineeRequest.setLastName("Ogiy");
        traineeRequest.setDateOfBirth(LocalDate.of(1991, 2, 2));
        traineeRequest.setAddress("79 Nikolska st");
        traineeRequest.setActive(true);
        var trainee = gymFacade.createTrainee(traineeRequest);

        var trainerRequest = new TrainerDTO();
        trainerRequest.setFirstName("Konstantin");
        trainerRequest.setLastName("Rubak");
        trainerRequest.setSpecialization("TypeScript");
        trainerRequest.setActive(true);
        var trainer = gymFacade.createTrainer(trainerRequest);

        var trainingRequest = new TrainingDTO();
        trainingRequest.setTrainerId(trainer.getId());
        trainingRequest.setTraineeIds(Arrays.asList(trainee.getId()));
        trainingRequest.setTrainingName("TypeScript");
        trainingRequest.setTrainingType("Learning TypeScript");
        trainingRequest.setTrainingDate(LocalDateTime.of(2026, Month.MAY, 15, 12, 15, 00));
        trainingRequest.setTrainingDuration(60);

        var training = gymFacade.createTraining(trainingRequest);

        assertNotNull(training);
        assertEquals("TypeScript", training.getTrainingName());
        assertEquals("Learning TypeScript", training.getTrainingType());
        assertEquals("60", training.getTrainingDuration());
    }

    @Test
    void testGetTraining() {
        var trainerDto = new TrainerDTO();
        trainerDto.setFirstName("Kelly");
        trainerDto.setLastName("Gregor");
        trainerDto.setSpecialization("TypeScript");
        trainerDto.setActive(true);
        Trainer trainer = gymFacade.createTrainer(trainerDto);

        var traineeRequest = new TraineeDTO();
        traineeRequest.setFirstName("Roman");
        traineeRequest.setLastName("Prokofev");
        traineeRequest.setDateOfBirth(LocalDate.of(1988, 11, 1));
        traineeRequest.setAddress("77 Manyilo st");
        traineeRequest.setActive(true);
        Trainee trainee = gymFacade.createTrainee(traineeRequest);

        var traineeRequest2 = new TraineeDTO();
        traineeRequest2.setFirstName("Anton");
        traineeRequest2.setLastName("Krunickiy");
        traineeRequest2.setDateOfBirth(LocalDate.of(1986, 1, 13));
        traineeRequest2.setAddress("23 Mostova st");
        traineeRequest2.setActive(true);
        Trainee trainee2 = gymFacade.createTrainee(traineeRequest2);

        var trainingRequest = new TrainingDTO();
        trainingRequest.setTrainerId(trainer.getId());
        trainingRequest.setTraineeIds(Arrays.asList(trainee.getId(), trainee2.getId()));
        trainingRequest.setTrainingName("TypeScript");
        trainingRequest.setTrainingType("Learning TypeScript");
        trainingRequest.setTrainingDate(LocalDateTime.of(2026, Month.MAY, 15, 12, 15, 00));
        trainingRequest.setTrainingDuration(90);

        var training = gymFacade.createTraining(trainingRequest);

        Training retrieved = gymFacade.findById(training.getId());

        assertNotNull(retrieved);
        assertEquals("TypeScript", retrieved.getTrainingName());
        assertEquals("Learning TypeScript", retrieved.getTrainingType());
        assertEquals("90", retrieved.getTrainingDuration());
    }*/
}
