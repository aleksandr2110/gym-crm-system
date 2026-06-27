package epam.service;

import epam.domain.Trainee;
import epam.domain.Trainer;
import epam.domain.Training;
import epam.repository.TraineeRepository;
import epam.repository.TrainerRepository;
import epam.repository.TrainingRepository;
import epam.request.TrainingDTO;
import epam.service.impl.TrainingServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TrainingServiceImplTest {

    @Mock
    private TrainingRepository trainingRepository;
    @Mock
    private TraineeRepository traineeRepository;
    @Mock
    private TrainerRepository trainerRepository;
    @InjectMocks
    private TrainingServiceImpl trainingService;

    @Test
    void shouldSaveTraining() {
        //GIVEN
        var training = new Training();
        training.setTrainingType("C#");
        training.setTrainingDate(LocalDateTime.of(2026, Month.OCTOBER, 13, 12, 15, 00));
        training.setTrainingDuration("45");
        training.setTrainingName("TypeScript");

        var trainingRequest = new TrainingDTO();
        trainingRequest.setTrainerId(1L);
        trainingRequest.setTraineeIds(Arrays.asList(1L, 2L));
        var trainer = new Trainer();
        var trainee = new Trainee();

        //WHEN
        when(trainerRepository.select(any())).thenReturn(trainer);
        when(traineeRepository.select(any())).thenReturn(trainee);
        when(trainingRepository.save(any())).thenReturn(training);
        var createdTraining = trainingService.create(training,
                trainingRequest.getTrainerId(), trainingRequest.getTraineeIds());

        //THEN
        assertNotNull(createdTraining);
        assertEquals(training.getTrainingDate(), createdTraining.getTrainingDate());
        assertEquals(training.getTrainingType(), createdTraining.getTrainingType());
        assertEquals(training.getTrainingDuration(), createdTraining.getTrainingDuration());
    }

    @Test
    void shouldSelectTrainingById() {
        //GIVEN
        Long userId = 2L;
        Training training = new Training();
        training.setTrainingType("C#");
        training.setTrainingDate(LocalDateTime.of(2027, Month.OCTOBER, 13, 12, 15, 00));
        training.setTrainingDuration("60");

        //WHEN
        when(trainingRepository.select(userId)).thenReturn(training);
        var createdTraining = trainingService.select(userId);

        //THEN
        assertNotNull(createdTraining);
        assertEquals(training.getTrainingDate(), createdTraining.getTrainingDate());
        assertEquals(training.getTrainingDuration(), createdTraining.getTrainingDuration());
    }
}
