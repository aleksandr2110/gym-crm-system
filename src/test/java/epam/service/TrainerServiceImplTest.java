package epam.service;

import epam.domain.Trainer;
import epam.repository.TrainerRepository;
import epam.request.TrainerDTO;
import epam.service.impl.TrainerServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TrainerServiceImplTest {

    @Mock
    private TrainerRepository trainerRepository;
    @InjectMocks
    private TrainerServiceImpl trainerService;

    @Test
    void shouldSaveTrainer() {
        //GIVEN
        var trainerRequest = new TrainerDTO();
        trainerRequest.setFirstName("Sergey");
        trainerRequest.setLastName("Hofman");
        trainerRequest.setSpecialization("PHP");
        trainerRequest.setActive(true);

        var trainer = new Trainer();
        trainer.setFirstName("Sergey");
        trainer.setLastName("Hofman");
        trainer.setSpecialization("PHP");
        trainer.setActive(true);

        //WHEN
        when(trainerRepository.save(any())).thenReturn(trainer);
        var createdTrainee = trainerService.create(trainerRequest);

        //THEN
        assertNotNull(createdTrainee);
        assertEquals(trainerRequest.getFirstName(), createdTrainee.getFirstName());
        assertEquals(trainerRequest.getLastName(), createdTrainee.getLastName());
        assertEquals(trainerRequest.getSpecialization(), createdTrainee.getSpecialization());
        assertTrue(createdTrainee.isActive());
        verify(trainerRepository, times(1)).save(any());
    }

    @Test
    void shouldUpdateTrainer() {
        //GIVEN
        Long userId = 1L;
        var trainerRequest = new TrainerDTO();
        trainerRequest.setFirstName("Alexandr");
        trainerRequest.setLastName("Kirichenko");
        trainerRequest.setSpecialization("PHP");
        trainerRequest.setActive(true);

        var currentTrainer = new Trainer();
        currentTrainer.setFirstName("Rerg");
        currentTrainer.setLastName("Grill");
        currentTrainer.setSpecialization("PHP");
        currentTrainer.setActive(true);

        var updatedTrainer = new Trainer();
        updatedTrainer.setFirstName(trainerRequest.getFirstName());
        updatedTrainer.setLastName(trainerRequest.getLastName());
        updatedTrainer.setSpecialization(trainerRequest.getSpecialization());
        updatedTrainer.setActive(trainerRequest.getActive());

        //WHEN
        when(trainerRepository.select(userId)).thenReturn(currentTrainer);
        when(trainerRepository.update(any())).thenReturn(updatedTrainer);
        var updatedTrainerById = trainerService.update(trainerRequest, userId);

        //THEN
        assertNotNull(updatedTrainerById);
        assertEquals(trainerRequest.getFirstName(), updatedTrainerById.getFirstName());
        assertEquals(trainerRequest.getLastName(), updatedTrainerById.getLastName());
        assertEquals(trainerRequest.getSpecialization(), updatedTrainerById.getSpecialization());
        assertTrue(updatedTrainerById.isActive());
    }

    @Test
    void shouldThrowExceptionWhenUpdateTrainer() {
        //GIVEN
        Long userId = 1L;
        Trainer empty = null;
        var trainerRequest = new TrainerDTO();

        //WHEN
        when(trainerRepository.select(userId)).thenReturn(empty);

        //THEN
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            trainerService.update(trainerRequest, userId);
        });
        assertEquals("User with id: " + userId + " not found!", exception.getMessage());
    }

    @Test
    void shouldSelectTrainerById() {
        //GIVEN
        Long userId = 1L;
        var trainer = new Trainer();
        trainer.setFirstName("Rerg");
        trainer.setLastName("Grill");
        trainer.setSpecialization("JavaScript");
        trainer.setActive(false);

        //WHEN
        when(trainerRepository.select(userId)).thenReturn(trainer);
        var selectedTrainerById = trainerService.select(userId);

        //THEN
        assertNotNull(selectedTrainerById);
        assertEquals(trainer.getFirstName(), selectedTrainerById.getFirstName());
        assertEquals(trainer.getLastName(), selectedTrainerById.getLastName());
        assertEquals(trainer.getSpecialization(), selectedTrainerById.getSpecialization());
        assertFalse(selectedTrainerById.isActive());
    }
}
