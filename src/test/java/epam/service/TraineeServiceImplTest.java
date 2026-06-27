package epam.service;

import epam.domain.Trainee;
import epam.repository.TraineeRepository;
import epam.service.impl.TraineeServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TraineeServiceImplTest {

    @Mock
    private TraineeRepository traineeRepository;
    @InjectMocks
    private TraineeServiceImpl traineeService;

    @Test
    void shouldSaveTrainee() {
        //GIVEN
        var traineeRequest = new Trainee();
        traineeRequest.setFirstName("Alex");
        traineeRequest.setLastName("Hofman");
        traineeRequest.setAddress("23 Road star st");
        traineeRequest.setActive(true);

        var trainee = new Trainee();
        trainee.setFirstName("Alex");
        trainee.setLastName("Hofman");
        trainee.setAddress("23 Road star st");
        trainee.setActive(true);

        //WHEN
        when(traineeRepository.save(any())).thenReturn(trainee);
        Trainee createdTrainee = traineeService.create(traineeRequest);

        //THEN
        assertNotNull(createdTrainee);
        assertEquals(traineeRequest.getFirstName(), createdTrainee.getFirstName());
        assertEquals(traineeRequest.getLastName(), createdTrainee.getLastName());
        assertEquals(traineeRequest.getAddress(), createdTrainee.getAddress());
        assertTrue(createdTrainee.isActive());
        verify(traineeRepository, times(1)).save(any());
    }

    @Test
    void shouldUpdateTrainee() {
        //GIVEN
        Long userId = 1L;
        var traineeRequest = new Trainee();
        traineeRequest.setFirstName("Alex");
        traineeRequest.setLastName("Hofman");
        traineeRequest.setAddress("23 Road star st");
        traineeRequest.setActive(true);

        var currentTrainee = new Trainee();
        currentTrainee.setFirstName("Rerg");
        currentTrainee.setLastName("Grill");
        currentTrainee.setAddress("23 Road star st");
        currentTrainee.setActive(true);

        var updatedTrainee = new Trainee();
        updatedTrainee.setFirstName(traineeRequest.getFirstName());
        updatedTrainee.setLastName(traineeRequest.getLastName());
        updatedTrainee.setAddress(traineeRequest.getAddress());
        updatedTrainee.setActive(traineeRequest.isActive());

        //WHEN
        when(traineeRepository.select(userId)).thenReturn(currentTrainee);
        when(traineeRepository.update(any())).thenReturn(updatedTrainee);
        Trainee updatedTraineeById = traineeService.update(traineeRequest, userId);

        //THEN
        assertNotNull(updatedTraineeById);
        assertEquals(traineeRequest.getFirstName(), updatedTraineeById.getFirstName());
        assertEquals(traineeRequest.getLastName(), updatedTraineeById.getLastName());
        assertEquals(traineeRequest.getAddress(), updatedTraineeById.getAddress());
        assertTrue(updatedTraineeById.isActive());
    }

    @Test
    void shouldThrowExceptionWhenUpdateTrainee() {
        //GIVEN
        Long userId = 1L;
        Trainee empty = null;
        var traineeRequest = new Trainee();

        //WHEN
        when(traineeRepository.select(userId)).thenReturn(empty);

        //THEN
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            traineeService.update(traineeRequest, userId);
        });
        assertEquals("User with id: " + userId + " not found!", exception.getMessage());
    }

    @Test
    void shouldSelectTraineeById() {
        //GIVEN
        Long userId = 1L;
        var trainee = new Trainee();
        trainee.setFirstName("Rerg");
        trainee.setLastName("Grill");
        trainee.setAddress("67 Road star st");
        trainee.setActive(false);

        //WHEN
        when(traineeRepository.select(userId)).thenReturn(trainee);
        var selectedTraineeById = traineeService.select(userId);

        //THEN
        assertNotNull(selectedTraineeById);
        assertEquals(trainee.getFirstName(), selectedTraineeById.getFirstName());
        assertEquals(trainee.getLastName(), selectedTraineeById.getLastName());
        assertEquals(trainee.getAddress(), selectedTraineeById.getAddress());
        assertFalse(selectedTraineeById.isActive());
    }

    @Test
    void shouldDeleteTraineeById() {
        //GIVEN
        Long userId = 1L;

        //WHEN
        traineeService.delete(userId);

        //THEN
        verify(traineeRepository, times(1)).delete(userId);
    }
}
