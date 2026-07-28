package epam.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import epam.application.FacadeGymCrmSystem;
import epam.controller.interfaces.TrainerController;
import epam.controller.rest.TrainerControllerImpl;
import epam.domain.dto.request.ChangePasswordRequestDTO;
import epam.domain.dto.request.TrainerRequestDTO;
import epam.domain.dto.request.UpdateTrainerRequestDTO;
import epam.domain.dto.response.RegistrationResponseDTO;
import epam.domain.dto.response.TrainerProfileDTO;
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

    @Mock
    private FacadeGymCrmSystem facadeGymCrmSystem;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        TrainerController controller = new TrainerControllerImpl(facadeGymCrmSystem);
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

        mockMvc.perform(post("/api/v1/trainers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value("Jastin.Trudo"))
                .andExpect(jsonPath("$.password").exists())
                .andReturn();
    }

    @Test
    void shouldReturnTrainerByUsername() throws Exception {
        var newTrainer = new TrainerRequestDTO();
        newTrainer.setFirstName("Jastin");
        newTrainer.setLastName("Trudo");
        newTrainer.setSpecialization("Python");;
        var registrationResponseDTO = deserializeFromJsonFile(
                "/trainer/registration-trainer-response.json", RegistrationResponseDTO.class);

        Mockito.when(facadeGymCrmSystem.createTrainer(Mockito.any())).thenReturn(registrationResponseDTO);

        MvcResult result = mockMvc.perform(post("/api/v1/trainers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newTrainer)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        var response = objectMapper.readValue(responseBody, RegistrationResponseDTO.class);
        String username = response.getUsername();

        var profileDTO = deserializeFromJsonFile(
                "/trainer/get-trainer-by-username.json", TrainerProfileDTO.class);

        Mockito.when(facadeGymCrmSystem.getTrainerByUsername(Mockito.any(), Mockito.any(),
                Mockito.any())).thenReturn(profileDTO);

        mockMvc.perform(get("/api/v1/trainers/" + username)
                        .header("X-Username", "Josh")
                        .header("X-Password", "34322ds"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value(username))
                .andExpect(jsonPath("$.firstName").value("Jastin"))
                .andExpect(jsonPath("$.lastName").value("Trudo"))
                .andExpect(jsonPath("$.specialization").value("Python"));
    }

    @Test
    public void shouldUpdateTrainerProfile() throws Exception {
        var request = new UpdateTrainerRequestDTO();
        request.setFirstName("Kim");
        request.setLastName("Starmer");
        request.setSpecialization("Python");

        var registrationResponseDTO = deserializeFromJsonFile(
                "/trainer/registration-trainer-response.json", RegistrationResponseDTO.class);
        Mockito.when(facadeGymCrmSystem.createTrainer(Mockito.any())).thenReturn(registrationResponseDTO);

        MvcResult registerResult = mockMvc.perform(post("/api/v1/trainers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        String registerResponseBody = registerResult.getResponse().getContentAsString();
        var registrationResponse = objectMapper.readValue(registerResponseBody, RegistrationResponseDTO.class);
        String username = registrationResponse.getUsername();

        String updateRequestJson = """
                {
                    "username": "%s",
                    "firstName": "Jeff",
                    "lastName": "Vitkof",
                    "specialization": "Python",
                    "isActive": false
                }
                """.formatted(username);
        var profile = new TrainerProfileDTO();
        profile.setUsername("Jastin.Trudo");
        profile.setFirstName("Jeff");
        profile.setLastName("Vitkof");
        profile.setSpecialization("Python");
        profile.setIsActive(false);

        Mockito.when(facadeGymCrmSystem.updateTrainerProfile(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(profile);

        mockMvc.perform(put("/api/v1/trainers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateRequestJson)
                        .header("X-Username", "Josh")
                        .header("X-Password", "3wwdqq"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value(username))
                .andExpect(jsonPath("$.firstName").value("Jeff"))
                .andExpect(jsonPath("$.lastName").value("Vitkof"))
                .andExpect(jsonPath("$.specialization").value("Python"))
                .andExpect(jsonPath("$.isActive").value(false));
    }

    @Test
    public void shouldChangePasswordTrainer() throws Exception {
        var newTrainer = new TrainerRequestDTO();
        newTrainer.setFirstName("Jastin");
        newTrainer.setLastName("Trudo");
        newTrainer.setSpecialization("Python");;
        var registrationResponseDTO = deserializeFromJsonFile(
                "/trainer/registration-trainer-response.json", RegistrationResponseDTO.class);

        Mockito.when(facadeGymCrmSystem.createTrainer(Mockito.any())).thenReturn(registrationResponseDTO);

        MvcResult registerResult = mockMvc.perform(post("/api/v1/trainers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newTrainer)))
                .andExpect(status().isCreated())
                .andReturn();

        String registrationResponse = registerResult.getResponse().getContentAsString();
        var registrationResp = objectMapper.readValue(registrationResponse, RegistrationResponseDTO.class);
        String username = registrationResp.getUsername();
        String oldPassword = registrationResp.getPassword();

        var changePasswordRequest = new ChangePasswordRequestDTO();
        changePasswordRequest.setUsername(username);
        changePasswordRequest.setOldPassword(oldPassword);
        changePasswordRequest.setNewPassword("newPas5421");

        doNothing().when(facadeGymCrmSystem).changeTrainerPassword(Mockito.any(), Mockito.any(),
                Mockito.any());

        mockMvc.perform(put("/api/v1/trainers/change-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changePasswordRequest))
                        .header("X-Username", "Josh")
                        .header("X-Password", "3wwdqq"))
                .andExpect(status().isOk());
        verify(facadeGymCrmSystem, times(1)).changeTrainerPassword(Mockito.any(), Mockito.any(),
                Mockito.any());
    }

    @Test
    public void shouldActivateTrainer() throws Exception {
        var newTrainer = new TrainerRequestDTO();
        newTrainer.setFirstName("Jastin");
        newTrainer.setLastName("Trudo");
        newTrainer.setSpecialization("Python");;
        var registrationResponseDTO = deserializeFromJsonFile(
                "/trainer/registration-trainer-response.json", RegistrationResponseDTO.class);

        Mockito.when(facadeGymCrmSystem.createTrainer(Mockito.any())).thenReturn(registrationResponseDTO);

        MvcResult registerResult = mockMvc.perform(post("/api/v1/trainers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newTrainer)))
                .andExpect(status().isCreated())
                .andReturn();

        String registrationResponse = registerResult.getResponse().getContentAsString();
        var registrationResp = objectMapper.readValue(registrationResponse, RegistrationResponseDTO.class);
        String username = registrationResp.getUsername();

        doNothing().when(facadeGymCrmSystem).activateDeactivateTrainer(username, true,
                "Jastin.Trudo", "34322ds");

        mockMvc.perform(patch("/api/v1/trainers/activation")
                        .param("username", username)
                        .param("isActive", "true")
                        .header("X-Username", "Jastin.Trudo")
                        .header("X-Password", "34322ds"))
                .andExpect(status().isOk());
        verify(facadeGymCrmSystem, times(1)).activateDeactivateTrainer(username, true,
                "Jastin.Trudo", "34322ds");
    }
}
