package epam.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import epam.controller.exception.ExceptionHandlerController;
import epam.controller.rest.TraineeControllerImpl;
import epam.domain.dto.request.ChangePasswordRequestDTO;
import epam.domain.dto.request.TraineeRequestDTO;
import epam.domain.dto.response.RegistrationResponseDTO;
import epam.service.TraineeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@WebAppConfiguration
@Transactional
public class TraineeControllerIT {

    @Autowired
    private TraineeControllerImpl traineeControllerImpl;

    @Autowired
    private TraineeService traineeService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(traineeControllerImpl)
                .setControllerAdvice(new ExceptionHandlerController())
                .build();
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
    }

    @Test
    void testShouldCreateTraineeAndReturnCredentials() throws Exception {
        var request = new TraineeRequestDTO();
        request.setFirstName("Jonn");
        request.setLastName("Kerry");
        request.setDateOfBirth(LocalDate.of(1988, 10, 13));
        request.setAddress("76 Red Av");

        MvcResult result = mockMvc.perform(post("/api/v1/trainees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value("Jonn.Kerry"))
                .andExpect(jsonPath("$.password").exists())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        var response = objectMapper.readValue(responseBody, RegistrationResponseDTO.class);

        assertNotNull(response.getUsername());
        assertNotNull(response.getPassword());
        assertEquals("Jonn.Kerry", response.getUsername());
        assertEquals(10, response.getPassword().length());
    }

    @Test
    void testShouldGenerateUniqueUsernameWhenDuplicateName() throws Exception {
        var request = new TraineeRequestDTO();
        request.setFirstName("Jonn");
        request.setLastName("Kerry");
        request.setDateOfBirth(LocalDate.of(1988, 10, 13));
        request.setAddress("76 Red Av");

        mockMvc.perform(post("/api/v1/trainees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("Jonn.Kerry"));

        var request2 = new TraineeRequestDTO();
        request2.setFirstName("Jonn");
        request2.setLastName("Kerry");

        MvcResult result = mockMvc.perform(post("/api/v1/trainees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request2)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("Jonn.Kerry1"))
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        var response = objectMapper.readValue(responseBody,
                RegistrationResponseDTO.class);

        assertEquals("Jonn.Kerry1", response.getUsername());
    }

    @Test
    void testShouldChangePasswordWithValidCredentials() throws Exception {
        var request = new TraineeRequestDTO();
        request.setFirstName("Martin");
        request.setLastName("Rossi");

        MvcResult registerResult = mockMvc.perform(post("/api/v1/trainees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        String registerResponseBody = registerResult.getResponse().getContentAsString();
        var registrationResponse = objectMapper.readValue(registerResponseBody,
                RegistrationResponseDTO.class);
        String username = registrationResponse.getUsername();
        String oldPassword = registrationResponse.getPassword();

        var changePasswordRequest = new ChangePasswordRequestDTO();
        changePasswordRequest.setUsername(username);
        changePasswordRequest.setOldPassword(oldPassword);
        changePasswordRequest.setNewPassword("newPassword678979897");

        mockMvc.perform(put("/api/v1/trainees/change-password")
                        .header("X-Username", username)
                        .header("X-Password", oldPassword)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changePasswordRequest)))
                .andExpect(status().isOk());

        var trainee = traineeService.findByUsername(username);
        assertNotNull(trainee);
        assertEquals("newPassword678979897", trainee.getPassword());
    }

    @Test
    void testShouldReturnErrorWhenChangePassword() throws Exception {
        var request = new TraineeRequestDTO();
        request.setFirstName("Maria");
        request.setLastName("Cary");

        MvcResult registerResult = mockMvc.perform(post("/api/v1/trainees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        String registerResponseBody = registerResult.getResponse().getContentAsString();
        var registrationResponse = objectMapper.readValue(registerResponseBody,
                RegistrationResponseDTO.class);
        String username = registrationResponse.getUsername();
        String oldPassword = registrationResponse.getPassword();

        var changePasswordRequest = new ChangePasswordRequestDTO();
        changePasswordRequest.setUsername(username);
        changePasswordRequest.setOldPassword("wrongPassword");
        changePasswordRequest.setNewPassword("newPassword678979897");

        mockMvc.perform(put("/api/v1/trainees/change-password")
                        .header("X-Username", username)
                        .header("X-Password", oldPassword)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changePasswordRequest)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testShouldReturnErrorWithNotExistedUser() throws Exception {
        var changePasswordRequest = new ChangePasswordRequestDTO();
        changePasswordRequest.setUsername("NotExisted");
        changePasswordRequest.setOldPassword("somePassword");
        changePasswordRequest.setNewPassword("newPassword7897");

        mockMvc.perform(put("/api/v1/trainees/change-password")
                        .header("X-Username", "NotExisted")
                        .header("X-Password", "somePassword")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changePasswordRequest)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testShouldGetTraineeProfile() throws Exception {
        var request = new TraineeRequestDTO();
        request.setFirstName("Anna");
        request.setLastName("Brown");
        request.setDateOfBirth(LocalDate.of(1993, 5, 19));
        request.setAddress("68 Rose Ave");

        MvcResult registerResult = mockMvc.perform(post("/api/v1/trainees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        String registerResponseBody = registerResult.getResponse().getContentAsString();
        var registrationResponse = objectMapper.readValue(registerResponseBody,
                RegistrationResponseDTO.class);
        String username = registrationResponse.getUsername();
        String password = registrationResponse.getPassword();

        mockMvc.perform(get("/api/v1/trainees/" + username)
                        .header("X-Username", username)
                        .header("X-Password", password))

                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value(username))
                .andExpect(jsonPath("$.firstName").value("Anna"))
                .andExpect(jsonPath("$.lastName").value("Brown"))
                .andExpect(jsonPath("$.dateOfBirth").exists())
                .andExpect(jsonPath("$.address").value("68 Rose Ave"))
                .andExpect(jsonPath("$.isActive").value(false))
                .andExpect(jsonPath("$.trainers").isArray());
    }

    @Test
    void testShouldGetErrorWithNotExistedUsername() throws Exception {
        mockMvc.perform(get("/api/v1/trainees/NonExistent.User")
                        .header("X-Username", "NonExistent.User")
                        .header("X-Password", "somePassword"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testShouldDeleteTraineeProfileSuccessfully() throws Exception {
        var request = new TraineeRequestDTO();
        request.setFirstName("Tim");
        request.setLastName("Shnaider");
        request.setDateOfBirth(LocalDate.of(1988, 7, 10));
        request.setAddress("76 Pine St");

        MvcResult registerResult = mockMvc.perform(post("/api/v1/trainees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        String registerResponseBody = registerResult.getResponse().getContentAsString();
        var registrationResponse = objectMapper.readValue(registerResponseBody,
                RegistrationResponseDTO.class);
        String username = registrationResponse.getUsername();
        String password = registrationResponse.getPassword();

        mockMvc.perform(delete("/api/v1/trainees/" + username)
                        .header("X-Username", username)
                        .header("X-Password", password))
                .andExpect(status().isNoContent());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> traineeService.findByUsername(username)
        );

        assertEquals("Trainee not found with username: " + username, exception.getMessage());
    }


    @Test
    void testShouldGetAvailableTrainersList() throws Exception {
        var request = new TraineeRequestDTO();
        request.setFirstName("Tim");
        request.setLastName("Shnaider");
        request.setDateOfBirth(LocalDate.of(1988, 7, 10));
        request.setAddress("76 Pine St");

        MvcResult registerResult = mockMvc.perform(post("/api/v1/trainees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        String registerResponseBody = registerResult.getResponse().getContentAsString();
        var registrationResponse = objectMapper.readValue(registerResponseBody,
                    RegistrationResponseDTO.class);
        String username = registrationResponse.getUsername();
        String password = registrationResponse.getPassword();

        mockMvc.perform(get("/api/v1/trainees/" + username + "/available-trainers")
                            .header("X-Username", username)
                            .header("X-Password", password))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$").isArray());
        }

    @Test
    void testShouldGetErrorWhenGetAvailableTrainersOnNotExistedTrainee() throws Exception {
        mockMvc.perform(get("/api/v1/trainees/NonExistent.User/available-trainers"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testShouldActivateTrainee() throws Exception {
        var request = new TraineeRequestDTO();
        request.setFirstName("Tim");
        request.setLastName("Shnaider");
        request.setDateOfBirth(LocalDate.of(1988, 7, 10));
        request.setAddress("76 Pine St");

        MvcResult registerResult = mockMvc.perform(post("/api/v1/trainees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        String registerResponseBody = registerResult.getResponse().getContentAsString();
        var registrationResponse = objectMapper.readValue(registerResponseBody,
                RegistrationResponseDTO.class);
        String username = registrationResponse.getUsername();
        String password = registrationResponse.getPassword();

        mockMvc.perform(patch("/api/v1/trainees/activation")
                        .header("X-Username", username)
                        .header("X-Password", password)
                        .param("username", username)
                        .param("isActive", "true"))
                .andExpect(status().isOk());

        var trainee = traineeService.findByUsername(username);
        assertNotNull(trainee);
        assertTrue(trainee.isActive());
    }

    @Test
    void testShouldDeactivateTrainee() throws Exception {
        var request = new TraineeRequestDTO();
        request.setFirstName("Tim");
        request.setLastName("Shnaider");
        request.setDateOfBirth(LocalDate.of(1988, 7, 10));
        request.setAddress("76 Pine St");

        MvcResult registerResult = mockMvc.perform(post("/api/v1/trainees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        String registerResponseBody = registerResult.getResponse().getContentAsString();
        var registrationResponse = objectMapper.readValue(registerResponseBody,
                RegistrationResponseDTO.class);
        String username = registrationResponse.getUsername();
        String password = registrationResponse.getPassword();

        mockMvc.perform(patch("/api/v1/trainees/activation")
                        .header("X-Username", username)
                        .header("X-Password", password)
                        .param("username", username)
                        .param("isActive", "false"))
                .andExpect(status().isOk());

        mockMvc.perform(patch("/api/v1/trainees/activation")
                        .header("X-Username", username)
                        .header("X-Password", password)
                        .param("username", username)
                        .param("isActive", "false"))
                .andExpect(status().isOk());

        var trainee = traineeService.findByUsername(username);
        assertNotNull(trainee);
        assertFalse(trainee.isActive());
    }

    @Test
    void testShouldUpdateTraineeProfile() throws Exception {
        var request = new TraineeRequestDTO();
        request.setFirstName("Tim");
        request.setLastName("Shnaider");
        request.setDateOfBirth(LocalDate.of(1988, 7, 10));
        request.setAddress("76 Pine St");

        MvcResult registerResult = mockMvc.perform(post("/api/v1/trainees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        String registerResponseBody = registerResult.getResponse().getContentAsString();
        var registrationResponse = objectMapper.readValue(registerResponseBody,
                RegistrationResponseDTO.class);
        String username = registrationResponse.getUsername();
        String password = registrationResponse.getPassword();

        String updateRequestJson = """
                {
                    "username": "%s",
                    "firstName": "Tim",
                    "lastName": "Shnaider",
                    "dateOfBirth": "1987-10-21",
                    "address": "60 Dnipro Ave",
                    "isActive": false
                }
                """.formatted(username);

        mockMvc.perform(put("/api/v1/trainees")
                        .header("X-Username", username)
                        .header("X-Password", password)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateRequestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value(username))
                .andExpect(jsonPath("$.firstName").value("Tim"))
                .andExpect(jsonPath("$.lastName").value("Shnaider"))
                .andExpect(jsonPath("$.address").value("60 Dnipro Ave"))
                .andExpect(jsonPath("$.isActive").value(false));

        var trainee = traineeService.findByUsername(username);
        assertNotNull(trainee);
        assertEquals("Tim", trainee.getFirstName());
        assertEquals("Shnaider", trainee.getLastName());
        assertEquals("60 Dnipro Ave", trainee.getAddress());
        assertFalse(trainee.isActive());
    }

    @Test
    void testShouldUpdateTrainersList() throws Exception {
        var request = new TraineeRequestDTO();
        request.setFirstName("Tim");
        request.setLastName("Shnaider");
        request.setDateOfBirth(LocalDate.of(1988, 7, 10));
        request.setAddress("76 Pine St");

        MvcResult traineeResult = mockMvc.perform(post("/api/v1/trainees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        String traineeResponseBody = traineeResult.getResponse().getContentAsString();
        var registrationResponse = objectMapper.readValue(traineeResponseBody,
                RegistrationResponseDTO.class);
        String traineeUsername = registrationResponse.getUsername();
        String password = registrationResponse.getPassword();

        String updateTrainersRequestJson = """
                {
                    "traineeUsername": "%s",
                    "trainerUsernames": []
                }
                """.formatted(traineeUsername);

        mockMvc.perform(put("/api/v1/trainees/trainers")
                        .header("X-Username", traineeUsername)
                        .header("X-Password", password)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateTrainersRequestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }

}

