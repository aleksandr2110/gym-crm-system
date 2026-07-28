package epam.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import epam.application.FacadeGymCrmSystem;
import epam.controller.interfaces.TrainingController;
import epam.controller.rest.TrainingControllerImpl;
import epam.service.TrainerService;
import epam.service.TrainingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/trainings")
@RequiredArgsConstructor
public class TrainingControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private TrainingService trainingService;
    @Mock
    private TrainerService trainerService;
    @Mock
    private FacadeGymCrmSystem facadeGymCrmSystem;

    @BeforeEach
    void setup() {
        TrainingController controller = new TrainingControllerImpl(trainingService, trainerService, facadeGymCrmSystem);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .build();
        objectMapper = new ObjectMapper();
    }
}
