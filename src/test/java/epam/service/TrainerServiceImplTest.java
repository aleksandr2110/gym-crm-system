package epam.service;

import epam.domain.*;
import epam.repository.TraineeRepository;
import epam.repository.TrainerRepository;
import epam.repository.TrainingRepository;
import epam.service.impl.TrainerServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TrainerServiceImplTest {

    @Mock
    private TraineeRepository traineeRepository;
    @Mock
    private TrainerRepository trainerRepository;
    @Mock
    private TrainingRepository trainingRepository;
    @Mock
    private TrainingTypeService trainingTypeService;
    @InjectMocks
    private TrainerServiceImpl trainerService;

    @Test
    void shouldSaveTrainer() {
        //GIVEN
        Long userId = 1L;
        var trainee = new Trainee();
        trainee.setId(userId);

        var training = new Training();
        training.setId(userId);
        training.setTrainingName("PHP");

        var trainerRequest = new Trainer();
        trainerRequest.setFirstName("Sergey");
        trainerRequest.setLastName("Hofman");

        var trainingType = new TrainingType();
        trainingType.setTrainingTypeName(TrainingTypeName.getByName("PHP"));

        trainerRequest.setSpecialization(trainingType);
        trainerRequest.setIsActive(true);
        trainerRequest.setTrainees(Arrays.asList(trainee));
        Set<Training> trainingSet = new LinkedHashSet<>();
        trainingSet.add(training);
        trainerRequest.setTrainings(trainingSet);

        var newTrainer = trainerRequest;

        //WHEN
        when(trainingTypeService.findByName(any())).thenReturn(trainingType);
        when(traineeRepository.findById(any())).thenReturn(trainee);
        when(trainingRepository.findTrainingById(any())).thenReturn(training);
        when(trainerRepository.save(any())).thenReturn(newTrainer);
        var createdTrainer = trainerService.save(trainerRequest);

        //THEN
        assertNotNull(createdTrainer);
        assertEquals(trainerRequest.getFirstName(), createdTrainer.getFirstName());
        assertEquals(trainerRequest.getLastName(), createdTrainer.getLastName());
        assertEquals(trainerRequest.getSpecialization(), createdTrainer.getSpecialization());
        assertTrue(createdTrainer.getIsActive());
        verify(trainerRepository, times(1)).save(any());
    }

    @Test
    void shouldUpdateTrainer() {
        //GIVEN
        Long userId = 1L;

        var training = new Training();
        training.setId(userId);
        training.setTrainingName("PHP");

        var trainingType = new TrainingType();
        trainingType.setTrainingTypeName(TrainingTypeName.getByName("PHP"));

        var trainerRequest = new Trainer();
        trainerRequest.setId(userId);
        trainerRequest.setFirstName("Alexandr");
        trainerRequest.setLastName("Kirichenko");
        trainerRequest.setSpecialization(trainingType);
        trainerRequest.setIsActive(true);

        var currentTrainer = new Trainer();
        currentTrainer.setId(userId);
        currentTrainer.setFirstName("Rerg");
        currentTrainer.setLastName("Grill");
        currentTrainer.setSpecialization(trainingType);
        currentTrainer.setIsActive(true);

        var updatedTrainer = new Trainer();
        updatedTrainer.setId(trainerRequest.getId());
        updatedTrainer.setFirstName(trainerRequest.getFirstName());
        updatedTrainer.setLastName(trainerRequest.getLastName());
        updatedTrainer.setSpecialization(trainerRequest.getSpecialization());
        updatedTrainer.setIsActive(trainerRequest.getIsActive());

        //WHEN
        when(trainerRepository.findById(userId)).thenReturn(currentTrainer);
        when(trainerRepository.updateProfile(currentTrainer)).thenReturn(updatedTrainer);
        var updatedNewTrainer = trainerService.updateProfile(trainerRequest, userId);

        //THEN
        assertNotNull(updatedNewTrainer);
        assertEquals(trainerRequest.getFirstName(), updatedNewTrainer.getFirstName());
        assertEquals(trainerRequest.getLastName(), updatedNewTrainer.getLastName());
        assertEquals(trainerRequest.getSpecialization(), updatedNewTrainer.getSpecialization());
        assertTrue(updatedNewTrainer.getIsActive());
    }

    @Test
    void shouldThrowExceptionWhenUpdateTrainer() {
        //GIVEN
        Long userId = 1L;
        Trainer emptyTrainee = null;
        var trainerRequest = new Trainer();
        trainerRequest.setId(userId);

        //WHEN
        when(trainerRepository.findById(userId)).thenReturn(emptyTrainee);

        //THEN
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            trainerService.updateProfile(trainerRequest, userId);
        });
        assertEquals("Trainee with id: " + userId + " not found!", exception.getMessage());
    }

    @Test
    void shouldSelectTrainerById() {
        //GIVEN
        Long userId = 1L;

        var trainer = new Trainer();
        trainer.setFirstName("Rerg");
        trainer.setLastName("Grill");
        var trainingType = new TrainingType();
        trainingType.setTrainingTypeName(TrainingTypeName.getByName("Java"));
        trainer.setSpecialization(trainingType);
        trainer.setIsActive(false);

        //WHEN
        when(trainerRepository.findById(userId)).thenReturn(trainer);
        var selectedTrainerById = trainerService.findById(userId);

        //THEN
        assertNotNull(selectedTrainerById);
        assertEquals(trainer.getFirstName(), selectedTrainerById.getFirstName());
        assertEquals(trainer.getLastName(), selectedTrainerById.getLastName());
        assertEquals(trainer.getSpecialization(), selectedTrainerById.getSpecialization());
        assertFalse(selectedTrainerById.getIsActive());
    }
}
