package epam;

import epam.application.FacadeGymCrmSystem;
import epam.config.ApplicationConfig;
import epam.config.DataSilosConfig;
import epam.domain.Trainee;
import epam.domain.Trainer;
import epam.domain.Training;
import epam.request.TraineeDTO;
import epam.request.TrainerDTO;
import epam.request.TrainingDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ApplicationConfig.class, DataSilosConfig.class})
public class GymFacadeTest {

    private FacadeGymCrmSystem gymFacade;
    private Map<Long, Trainee> traineeStorage;
    private Map<Long, Trainer> trainerStorage;

    @Autowired
    public void setGymFacade(FacadeGymCrmSystem gymFacade) {
        this.gymFacade = gymFacade;
    }

    @Autowired
    public void setTraineeStorage(Map<Long, Trainee> traineeStorage) {
        this.traineeStorage = traineeStorage;
    }

    @Autowired
    public void setTrainerStorage(Map<Long, Trainer> trainerStorage) {
        this.trainerStorage = trainerStorage;
    }


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

        Trainee created = gymFacade.createTrainee(traineeDTO);
        Long userId = created.getUserId();
        Trainee retrieved = gymFacade.getTrainee(userId);

        assertNotNull(retrieved);
        assertEquals(userId, retrieved.getUserId());
        assertEquals("Jastin", retrieved.getFirstName());
        assertEquals("Timbarlake", retrieved.getLastName());
        assertEquals(LocalDate.of(1984, 3, 14), retrieved.getDateOfBirth());
        assertEquals("11/2 Dark Ave", retrieved.getAddress());
        assertNotNull("Jastin.Timbarlake", retrieved.getUserName());
    }

    @Test
    void testGetTraineeById() {
        Long userId = 5L;
        Trainee retrieved = gymFacade.getTrainee(userId);
        assertEquals(userId, retrieved.getUserId());
        assertNotNull("Emilia.Kavi", retrieved.getUserName());
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
        Long userId = created.getUserId();

        TraineeDTO updateDto = new TraineeDTO();
        updateDto.setFirstName("Berg");
        updateDto.setLastName("Beatson");
        updateDto.setDateOfBirth(LocalDate.of(1981, 5, 11));
        updateDto.setAddress("43 Stone St");
        updateDto.setActive(true);

        Trainee updatedTrainee = gymFacade.updateTrainee(updateDto, userId);

        assertNotNull(updatedTrainee);
        assertEquals("Berg", updatedTrainee.getFirstName());
        assertEquals("Beatson", updatedTrainee.getLastName());
        assertEquals("43 Stone St", updatedTrainee.getAddress());
        assertEquals(LocalDate.of(1981, 5, 11), updatedTrainee.getDateOfBirth());
        assertNotNull("Berg.Beatson", updatedTrainee.getUserName());
    }

    @Test
    void testCreateTrainer() {
        var trainerDto = new TrainerDTO();
        trainerDto.setFirstName("Evgeniy");
        trainerDto.setLastName("Syleimanov");
        trainerDto.setSpecialization("Java");
        trainerDto.setActive(true);

        Trainer createdTrainer = gymFacade.createTrainer(trainerDto);

        assertNotNull(createdTrainer);
        assertEquals("Java", createdTrainer.getSpecialization());
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
        Long userId = created.getUserId();

        Trainer retrievedTrainer = gymFacade.getTrainer(userId);

        assertNotNull(retrievedTrainer);
        assertEquals(userId, retrievedTrainer.getUserId());
        assertEquals("Java", retrievedTrainer.getSpecialization());
    }

    @Test
    void testUpdateTrainer() {
        var trainerRequest = new TrainerDTO();
        trainerRequest.setFirstName("Alexey");
        trainerRequest.setLastName("Litovchenko");
        trainerRequest.setSpecialization("C++");
        trainerRequest.setActive(true);

        Trainer created = gymFacade.createTrainer(trainerRequest);
        Long userId = created.getUserId();

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
        trainingRequest.setTrainerId(trainer.getUserId());
        trainingRequest.setTraineeIds(Arrays.asList(trainee.getUserId()));
        trainingRequest.setTrainingName("TypeScript");
        trainingRequest.setTrainingType("Learning TypeScript");
        trainingRequest.setTrainingDate(LocalDateTime.of(2026, Month.MAY, 15, 12, 15, 00));
        trainingRequest.setTrainingDuration("60");

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
        trainingRequest.setTrainerId(trainer.getUserId());
        trainingRequest.setTraineeIds(Arrays.asList(trainee.getUserId(), trainee2.getUserId()));
        trainingRequest.setTrainingName("TypeScript");
        trainingRequest.setTrainingType("Learning TypeScript");
        trainingRequest.setTrainingDate(LocalDateTime.of(2026, Month.MAY, 15, 12, 15, 00));
        trainingRequest.setTrainingDuration("90");

        var training = gymFacade.createTraining(trainingRequest);

        Training retrieved = gymFacade.getTraining(training.getId());

        assertNotNull(retrieved);
        assertEquals("TypeScript", retrieved.getTrainingName());
        assertEquals("Learning TypeScript", retrieved.getTrainingType());
        assertEquals("90", retrieved.getTrainingDuration());
    }
}
