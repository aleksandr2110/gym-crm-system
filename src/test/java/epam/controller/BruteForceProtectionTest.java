package epam.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import epam.domain.dto.request.LoginRequest;
import epam.domain.dto.request.TraineeRequestDTO;
import epam.security.service.LoginAttemptService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class BruteForceProtectionTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LoginAttemptService loginAttemptService;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private EntityManager entityManager;

    private static final String LOGIN_ENDPOINT = "/api/auth/login";
    private static final String TEST_USERNAME = "Test.Username";
    private static final String WRONG_PASSWORD = "wrongpassword";

    @BeforeEach
    void setUp() throws Exception  {
        entityManager.createNativeQuery("DELETE FROM trainees").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM users").executeUpdate();
        entityManager.flush();
        var request = new TraineeRequestDTO();
        request.setFirstName("Test");
        request.setLastName("Username");
        request.setDateOfBirth(LocalDate.of(1988, 7, 10));
        request.setAddress("76 Pine St");
        mockMvc.perform(post("/api/v1/trainees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        loginAttemptService.loginSucceeded(TEST_USERNAME);
    }

    @Test
    void testShouldBlockAfterThreeFailedAttempts() throws Exception {
        LoginRequest loginRequest = new LoginRequest(TEST_USERNAME, WRONG_PASSWORD);
        String requestBody = objectMapper.writeValueAsString(loginRequest);

        mockMvc.perform(post(LOGIN_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(post(LOGIN_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(post(LOGIN_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(post(LOGIN_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isLocked())
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error").value(org.hamcrest.Matchers.containsString("locked")));
    }

    @Test
    void testFilterShouldBlockBeforeAuthentication() throws Exception {
        LoginRequest loginRequest = new LoginRequest(TEST_USERNAME, WRONG_PASSWORD);
        String requestBody = objectMapper.writeValueAsString(loginRequest);

        for (int i = 0; i < 3; i++) {
            mockMvc.perform(post(LOGIN_ENDPOINT)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isUnauthorized());
        }

        mockMvc.perform(post(LOGIN_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isLocked())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value(org.hamcrest.Matchers.containsString("minute")));
    }

    @Test
    void testShouldShowRemainingTime() throws Exception {
        LoginRequest loginRequest = new LoginRequest(TEST_USERNAME, WRONG_PASSWORD);
        String requestBody = objectMapper.writeValueAsString(loginRequest);

        for (int i = 0; i < 3; i++) {
            mockMvc.perform(post(LOGIN_ENDPOINT)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isUnauthorized());
        }

        mockMvc.perform(post(LOGIN_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isLocked())
                .andExpect(jsonPath("$.error").value(org.hamcrest.Matchers.matchesRegex(
                        ".*[0-9]+ minute\\(s\\) & [0-9]+ second\\(s\\).*")));
    }
}
