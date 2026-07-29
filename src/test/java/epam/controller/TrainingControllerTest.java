package epam.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import epam.application.FacadeGymCrmSystem;
import epam.controller.interfaces.TrainingController;
import epam.controller.rest.TrainingControllerImpl;
import epam.domain.dto.request.TrainingRequestDTO;
import epam.domain.dto.response.TrainingTypeDTO;
import epam.service.TrainerService;
import epam.service.TrainingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class TrainingControllerTest {

    @Mock
    private TrainingService trainingService;
    @Mock
    private TrainerService trainerService;
    @Mock
    private FacadeGymCrmSystem facadeGymCrmSystem;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        TrainingController controller = new TrainingControllerImpl(trainingService, trainerService, facadeGymCrmSystem);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void getTrainingTypes() throws Exception {
        var trainingType1 = new TrainingTypeDTO();
        trainingType1.setId(1L);
        trainingType1.setTrainingTypeName("Java");
        var trainingType2 = new TrainingTypeDTO();
        trainingType2.setId(2L);
        trainingType2.setTrainingTypeName("Python");
        var trainingType3 = new TrainingTypeDTO();
        trainingType3.setId(3L);
        trainingType3.setTrainingTypeName("JavaScript");
        List<TrainingTypeDTO> types = new ArrayList<>();
        types.add(trainingType1);
        types.add(trainingType2);
        types.add(trainingType3);

        Mockito.when(facadeGymCrmSystem.getTrainingTypes()).thenReturn(types);

        mockMvc.perform(get("/api/v1/trainings/types"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].trainingTypeName").exists());
    }

    @Test
    public void shouldReturnCreatedTraining() throws Exception {
        TrainingRequestDTO trainingRequest = new TrainingRequestDTO();
        trainingRequest.setTraineeUsername("Kevin.Josh");
        trainingRequest.setTrainerUsername("Jeff.Gosling");
        trainingRequest.setTrainingName("Java learning");
        trainingRequest.setTrainingType("Java");
        trainingRequest.setTrainingDate("2026-07-29T18:30:00");
        trainingRequest.setTrainingDuration(60);

        Mockito.doNothing().when(facadeGymCrmSystem).createTraining(Mockito.any(), Mockito.any(), Mockito.any());

        mockMvc.perform(post("/api/v1/trainings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trainingRequest))
                        .header("X-Username", "Josh")
                        .header("X-Password", "34322ds"))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        verify(facadeGymCrmSystem, times(1)).createTraining(Mockito.any(),
                Mockito.any(), Mockito.any());
    }

    @Test
    public void shouldReturnTraineeTraining() throws Exception {

    }
}
