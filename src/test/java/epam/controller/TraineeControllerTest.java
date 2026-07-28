package epam.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import epam.application.FacadeGymCrmSystem;
import epam.controller.interfaces.TraineeController;
import epam.controller.rest.TraineeControllerImpl;
import epam.domain.dto.request.ChangePasswordRequestDTO;
import epam.domain.dto.request.TraineeRequestDTO;
import epam.domain.dto.response.RegistrationResponseDTO;
import epam.domain.dto.response.TraineeProfileDTO;
import epam.domain.dto.response.TrainerInfoDTO;
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

import java.util.List;

import static epam.util.TestUtil.deserializeFromJsonFile;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class TraineeControllerTest {

    @Mock
    private TraineeService traineeService;
    @Mock
    private TrainerService trainerService;
    @Mock
    private FacadeGymCrmSystem facadeGymCrmSystem;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        TraineeController controller = new TraineeControllerImpl(traineeService, trainerService, facadeGymCrmSystem);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void shouldReturnCreatedTrainee() throws Exception {
        var newTrainee = new TraineeRequestDTO();
        newTrainee.setFirstName("Josh");
        newTrainee.setLastName("Builder");
        newTrainee.setAddress("434 fly road st");
        var registrationResponseDTO = deserializeFromJsonFile(
                "/trainee/registration-trainee-response.json", RegistrationResponseDTO.class);

        Mockito.when(facadeGymCrmSystem.createTrainee(Mockito.any())).thenReturn(registrationResponseDTO);

        mockMvc.perform(post("/api/v1/trainees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newTrainee)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("Josh.Builder"))
                .andExpect(jsonPath("$.password").value("fdsfdsf798"));
    }

    @Test
    void shouldReturnTraineeByUsername() throws Exception {
        var newTrainee = new TraineeRequestDTO();
        newTrainee.setFirstName("Josh");
        newTrainee.setLastName("Builder");
        newTrainee.setAddress("434 fly road st");
        var registrationResponseDTO = deserializeFromJsonFile(
                "/trainee/registration-trainee-response.json", RegistrationResponseDTO.class);

        Mockito.when(facadeGymCrmSystem.createTrainee(Mockito.any())).thenReturn(registrationResponseDTO);

        MvcResult registerResult = mockMvc.perform(post("/api/v1/trainees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newTrainee)))
                .andExpect(status().isCreated())
                .andReturn();

        String registrationResponse = registerResult.getResponse().getContentAsString();
        var registrationResp = objectMapper.readValue(registrationResponse, RegistrationResponseDTO.class);
        String username = registrationResp.getUsername();

        var profileDTO = deserializeFromJsonFile(
                "/trainee/get-trainee-by-username.json", TraineeProfileDTO.class);
        Mockito.when(facadeGymCrmSystem.getTraineeByUsername(Mockito.any(), Mockito.any(),
                Mockito.any())).thenReturn(profileDTO);

        mockMvc.perform(get("/api/v1/trainees/" + username)
                        .header("X-Username", "Josh")
                        .header("X-Password", "34322ds"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value(username))
                .andExpect(jsonPath("$.firstName").value("Josh"))
                .andExpect(jsonPath("$.lastName").value("Builder"))
                .andExpect(jsonPath("$.dateOfBirth").exists())
                .andExpect(jsonPath("$.address").value("11 Green st"))
                .andExpect(jsonPath("$.isActive").value(false))
                .andExpect(jsonPath("$.trainers").isArray());
    }

    @Test
    void shouldDeleteTraineeByUsername() throws Exception  {
        var newTrainee = new TraineeRequestDTO();
        newTrainee.setFirstName("Josh");
        newTrainee.setLastName("Builder");
        newTrainee.setAddress("434 fly road st");
        var registrationResponseDTO = deserializeFromJsonFile(
                "/trainee/registration-trainee-response.json", RegistrationResponseDTO.class);

        Mockito.when(facadeGymCrmSystem.createTrainee(Mockito.any())).thenReturn(registrationResponseDTO);

        MvcResult registerResult = mockMvc.perform(post("/api/v1/trainees")
                        .header("X-Username", "Josh")
                        .header("X-Password", "34322ds")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newTrainee)))
                .andExpect(status().isCreated())
                .andReturn();

        String registrationResponse = registerResult.getResponse().getContentAsString();
        var registrationResp = objectMapper.readValue(registrationResponse, RegistrationResponseDTO.class);
        String username = registrationResp.getUsername();

        doNothing().when(facadeGymCrmSystem).deleteTrainee(Mockito.any(), Mockito.any(), Mockito.any());

        mockMvc.perform(delete("/api/v1/trainees/" + username)
                        .header("X-Username", "Josh")
                        .header("X-Password", "34322ds"))
                .andExpect(status().isOk());

        verify(facadeGymCrmSystem, times(1)).deleteTrainee(Mockito.any(), Mockito.any(), Mockito.any());
    }

    @Test
    void shouldGetAvailableTrainers() throws Exception {
        var newTrainee = new TraineeRequestDTO();
        newTrainee.setFirstName("Josh");
        newTrainee.setLastName("Builder");
        newTrainee.setAddress("434 fly road st");
        var registrationResponseDTO = deserializeFromJsonFile(
                "/trainee/registration-trainee-response.json", RegistrationResponseDTO.class);

        Mockito.when(facadeGymCrmSystem.createTrainee(Mockito.any())).thenReturn(registrationResponseDTO);

        MvcResult registerResult = mockMvc.perform(post("/api/v1/trainees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newTrainee)))
                .andExpect(status().isCreated())
                .andReturn();

        String registrationResponse = registerResult.getResponse().getContentAsString();
        var registrationResp = objectMapper.readValue(registrationResponse, RegistrationResponseDTO.class);
        String username = registrationResp.getUsername();

        Mockito.when(facadeGymCrmSystem.getAvailableTrainers(Mockito.any(), Mockito.any(),
                Mockito.any())).thenReturn(createTrainers());

        mockMvc.perform(get("/api/v1/trainees/" + username + "/available-trainers")
                        .header("X-Username", "Josh")
                        .header("X-Password", "34322ds"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void shouldUpdateTraineeProfile() throws Exception {
        var newTrainee = new TraineeRequestDTO();
        newTrainee.setFirstName("Josh");
        newTrainee.setLastName("Builder");
        newTrainee.setAddress("434 fly road st");
        var registrationResponseDTO = deserializeFromJsonFile(
                "/trainee/registration-trainee-response.json", RegistrationResponseDTO.class);

        Mockito.when(facadeGymCrmSystem.createTrainee(Mockito.any())).thenReturn(registrationResponseDTO);

        MvcResult registerResult = mockMvc.perform(post("/api/v1/trainees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newTrainee)))
                .andExpect(status().isCreated())
                .andReturn();

        String registrationResponse = registerResult.getResponse().getContentAsString();
        var registrationResp = objectMapper.readValue(registrationResponse, RegistrationResponseDTO.class);
        String username = registrationResp.getUsername();

        String updateRequestJson = """
                {
                    "username": "%s",
                    "firstName": "James",
                    "lastName": "Halpert",
                    "dateOfBirth": "1987-11-01",
                    "address": "455 Light Ave",
                    "isActive": false
                }
                """.formatted(username);

        var profile = new TraineeProfileDTO();
        profile.setUsername("Josh.Builder");
        profile.setFirstName("James");
        profile.setLastName("Halpert");
        profile.setAddress("455 Light Ave");
        profile.setIsActive(false);

        Mockito.when(facadeGymCrmSystem.updateTraineeProfile(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(profile);

        mockMvc.perform(put("/api/v1/trainees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateRequestJson)
                        .header("X-Username", "Josh")
                        .header("X-Password", "34322ds"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value(username))
                .andExpect(jsonPath("$.firstName").value("James"))
                .andExpect(jsonPath("$.lastName").value("Halpert"))
                .andExpect(jsonPath("$.address").value("455 Light Ave"))
                .andExpect(jsonPath("$.isActive").value(false));
    }

    @Test
    public void shouldChangePasswordTrainee() throws Exception {
        var newTrainee = new TraineeRequestDTO();
        newTrainee.setFirstName("Josh");
        newTrainee.setLastName("Builder");
        newTrainee.setAddress("434 fly road st");
        var registrationResponseDTO = deserializeFromJsonFile(
                "/trainee/registration-trainee-response.json", RegistrationResponseDTO.class);

        Mockito.when(facadeGymCrmSystem.createTrainee(Mockito.any())).thenReturn(registrationResponseDTO);

        MvcResult registerResult = mockMvc.perform(post("/api/v1/trainees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newTrainee)))
                .andExpect(status().isCreated())
                .andReturn();

        String registrationResponse = registerResult.getResponse().getContentAsString();
        var registrationResp = objectMapper.readValue(registrationResponse, RegistrationResponseDTO.class);
        String username = registrationResp.getUsername();
        String oldPassword = registrationResp.getPassword();

        var changePasswordRequest = new ChangePasswordRequestDTO();
        changePasswordRequest.setUsername(username);
        changePasswordRequest.setOldPassword(oldPassword);
        changePasswordRequest.setNewPassword("newPass5435");

        doNothing().when(facadeGymCrmSystem).changeTraineePassword(Mockito.any(), Mockito.any(),
                Mockito.any());

        mockMvc.perform(put("/api/v1/trainees/change-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changePasswordRequest))
                        .header("X-Username", "Josh")
                        .header("X-Password", "34322ds"))
                .andExpect(status().isOk());
        verify(facadeGymCrmSystem, times(1)).changeTraineePassword(Mockito.any(), Mockito.any(),
                Mockito.any());
    }

    @Test
    public void shouldActivateTrainee() throws Exception {
        var newTrainee = new TraineeRequestDTO();
        newTrainee.setFirstName("Josh");
        newTrainee.setLastName("Builder");
        newTrainee.setAddress("434 fly road st");
        var registrationResponseDTO = deserializeFromJsonFile(
                "/trainee/registration-trainee-response.json", RegistrationResponseDTO.class);

        Mockito.when(facadeGymCrmSystem.createTrainee(Mockito.any())).thenReturn(registrationResponseDTO);

        MvcResult registerResult = mockMvc.perform(post("/api/v1/trainees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newTrainee)))
                .andExpect(status().isCreated())
                .andReturn();

        String registrationResponse = registerResult.getResponse().getContentAsString();
        var registrationResp = objectMapper.readValue(registrationResponse, RegistrationResponseDTO.class);
        String username = registrationResp.getUsername();

        doNothing().when(facadeGymCrmSystem).activateDeactivateTrainee(username, true,
                "Josh", "34322ds");

        mockMvc.perform(patch("/api/v1/trainees/activation")
                        .param("username", username)
                        .param("isActive", "true")
                        .header("X-Username", "Josh")
                        .header("X-Password", "34322ds"))
                .andExpect(status().isOk());

        verify(facadeGymCrmSystem, times(1)).activateDeactivateTrainee(username, true,
                "Josh", "34322ds");
    }

    private static List<TrainerInfoDTO> createTrainers() {
        List<TrainerInfoDTO> trainers = List.of(new TrainerInfoDTO(1L, "Jonson.Gosh", "Jonson",
                "Gosh", "Java"), new TrainerInfoDTO(2L, "Jack.Gosh", "Jack",
                "Gosh", "JavaScript"));
        return trainers;
    }

    /*@Test
    // "2026-07-26T15:30:00",

    void shouldSortByCompanyNameAndAsc() {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(getVacanciesSortByAndOrderBy())
                .queryParam("pageNumber", 0)
                .queryParam("pageSize", 4)
                .queryParam("sortBy", "companyName")
                .queryParam("orderBy", "asc");
        var responseDto = testRestTemplate.getForObject(uriBuilder.toUriString(), DataObjectDTO.class);

        List<DataDTO> responseList = responseDto.getDataList();
        // AHV Deutschland GmbH GAB-Solution GmbH PrettyTELCO GmbH PrettyTELCO GmbH ASC page 0
        assertEquals(4, responseDto.getDataList().size());
        assertEquals("AHV Deutschland GmbH", responseList.get(0).getCompanyName());
        assertEquals("GAB-Solution GmbH", responseList.get(1).getCompanyName());
    }*/
}
