package epam.service;

import epam.domain.InnerDataTraining;
import epam.domain.Training;
import epam.repository.TrainingRepository;
import epam.request.TraineeRequest;
import epam.request.TrainerRequest;
import epam.request.TrainingRequest;
import epam.service.impl.TrainingServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TrainingServiceImplTest {

    @Mock
    private TrainingRepository trainingRepository;
    @InjectMocks
    private TrainingServiceImpl trainingService;

    @Test
    void shouldSaveTraining() {
        //GIVEN
        /*var traineeRequest = new TraineeRequest();
        traineeRequest.setFirstName("Sabrina");
        traineeRequest.setLastName("Ogiy");
        traineeRequest.setDateOfBirth(LocalDate.of(1991, 2, 2));
        traineeRequest.setAddress("79 Nikolska st");

        var trainerRequest = new TrainerRequest();
        trainerRequest.setFirstName("Konstantin");
        trainerRequest.setLastName("Rubak");
        trainerRequest.setSpecialization("TypeScript");
        trainerRequest.setActive(true); */

        Training training = new Training();
        training.setTrainingType("C#");
        training.setTrainingDate(LocalDateTime.of(2026, Month.OCTOBER, 13, 12, 15, 00));
        training.setTrainingDuration("45");

        var trainingRequest = new TrainingRequest();
        var innerDataTraining = new InnerDataTraining();
        trainingRequest.setInnerDataTraining(innerDataTraining);

        //WHEN
        when(trainingRepository.save(any())).thenReturn(training);
        var createdTraining = trainingService.create(trainingRequest);

        //THEN
        assertNotNull(createdTraining);
        assertEquals(training.getTrainingDate(), createdTraining.getTrainingDate());
        assertEquals(training.getTrainingType(), createdTraining.getTrainingType());
        assertEquals(training.getTrainingDuration(), createdTraining.getTrainingDuration());
    }

    @Test
    void shouldSelectTrainingById() {
        //GIVEN
        Training training = new Training();
        training.setTrainingType("C#");
        training.setTrainingDate(LocalDateTime.of(2027, Month.OCTOBER, 13, 12, 15, 00));
        training.setTrainingDuration("60");

        var innerDataTraining = new InnerDataTraining();
        innerDataTraining.setTrainingName("Practice C#");

        //WHEN
        when(trainingRepository.select(innerDataTraining)).thenReturn(training);
        var createdTraining = trainingService.select(innerDataTraining);

        //THEN
        assertNotNull(createdTraining);
        assertEquals(training.getTrainingDate(), createdTraining.getTrainingDate());
        assertEquals(training.getTrainingDuration(), createdTraining.getTrainingDuration());
    }
}
