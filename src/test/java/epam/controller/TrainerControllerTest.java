package epam.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import epam.application.FacadeGymCrmSystem;
import epam.controller.interfaces.TraineeController;
import epam.controller.interfaces.TrainerController;
import epam.controller.rest.TraineeControllerImpl;
import epam.controller.rest.TrainerControllerImpl;
import epam.domain.dto.request.TrainerRequestDTO;
import epam.domain.dto.response.RegistrationResponseDTO;
import epam.service.TraineeService;
import epam.service.TrainerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static epam.util.TestUtil.deserializeFromJsonFile;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class TrainerControllerTest {

//    @Mock
//    private TraineeService traineeService;
    @Mock
    private TrainerService trainerService;
    @Mock
    private FacadeGymCrmSystem facadeGymCrmSystem;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        TrainerController controller = new TrainerControllerImpl(trainerService, facadeGymCrmSystem);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void shouldReturnCreatedTrainer() throws Exception {
        var request = new TrainerRequestDTO();
        request.setFirstName("Jastin");
        request.setLastName("Trudo");
        request.setSpecialization("Python");
        var registrationResponseDTO = deserializeFromJsonFile(
                "/trainer/registration-trainer-response.json", RegistrationResponseDTO.class);

        Mockito.when(facadeGymCrmSystem.createTrainer(Mockito.any())).thenReturn(registrationResponseDTO);

        MvcResult result = mockMvc.perform(post("/api/v1/trainers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value("Jastin.Trudo"))
                .andExpect(jsonPath("$.password").exists())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        var response = objectMapper.readValue(responseBody, RegistrationResponseDTO.class);
    }

}
