package epam.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import epam.controller.exception.ExceptionHandlerController;
import epam.controller.rest.TrainingControllerImpl;
import epam.domain.entity.Trainee;
import epam.domain.entity.Trainer;
import epam.service.TraineeService;
import epam.service.TrainerService;
import epam.service.TrainingTypeService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@WebAppConfiguration
@Transactional
public class TrainingControllerIT {

    @Autowired
    private TrainingControllerImpl trainingControllerImpl;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private TraineeService traineeService;

    @Autowired
    private TrainerService trainerService;

    @Autowired
    private TrainingTypeService trainingTypeService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(trainingControllerImpl)
                .setControllerAdvice(new ExceptionHandlerController())
                .build();
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

        entityManager.createNativeQuery("DELETE FROM trainings").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM trainers_trainees").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM trainees").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM trainers").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM users").executeUpdate();
        entityManager.flush();
    }

    @Test
    void testShouldReturnAllTrainingTypes() throws Exception {
        mockMvc.perform(get("/api/v1/trainings/types"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].trainingTypeName").value("JAVA"))
                .andExpect(jsonPath("$[1].trainingTypeName").value("C"))
                .andExpect(jsonPath("$[2].trainingTypeName").value("PYTHON"))
                .andExpect(jsonPath("$[10].trainingTypeName").value("DEVOPS"));
    }

    @Test
    void testShouldCreateTrainingSuccessfully() throws Exception {
        String firstSpecialization = "Python";
        var trainee = new Trainee();
        trainee.setFirstName("Rihard");
        trainee.setLastName("Brown");

        var traineeResponse = traineeService.save(trainee);
        String traineeUsername = traineeResponse.getUsername();

        var trainer = new Trainer();
        trainer.setFirstName("Tim");
        trainer.setLastName("Shnaider");
        var trainingType = trainingTypeService.findByName(firstSpecialization.toUpperCase());
        trainer.setSpecialization(trainingType);
        var trainerResponse = trainerService.save(trainer, firstSpecialization);
        String trainerUsername = trainerResponse.getUsername();
        String trainerPassword = trainerResponse.getPassword();

        String trainingRequestJson = """
                {
                    "traineeUsername": "%s",
                    "trainerUsername": "%s",
                    "trainingName": "Python learning",
                    "trainingType": "Python",
                    "trainingDate": "2026-08-15T12:15",
                    "trainingDuration": 60
                }
                """.formatted(traineeUsername, trainerUsername);

        mockMvc.perform(post("/api/v1/trainings")
                        .header("X-Username", trainerUsername)
                        .header("X-Password", trainerPassword)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(trainingRequestJson))
                .andExpect(status().isOk());
    }

    @Test
    void testShouldGetTraineeTrainings() throws Exception {
        String specialization = "Python";
        var traineeRequest = new Trainee();
        traineeRequest.setFirstName("Casey");
        traineeRequest.setLastName("Sherman");
        var traineeResponse = traineeService.save(traineeRequest);
        String traineeUsername = traineeResponse.getUsername();
        String traineePassword = traineeResponse.getPassword();

        var trainer = new Trainer();
        trainer.setFirstName("Jane");
        trainer.setLastName("Smith");
        var trainingType = trainingTypeService.findByName(specialization.toUpperCase());
        trainer.setSpecialization(trainingType);
        var trainerResponse = trainerService.save(trainer, specialization);
        String trainerUsername = trainerResponse.getUsername();
        String trainerPassword = trainerResponse.getPassword();

        String trainingRequestJson = """
                {
                    "traineeUsername": "%s",
                    "trainerUsername": "%s",
                    "trainingName": "Practice Python",
                    "trainingType": "Python",
                    "trainingDate": "2026-08-15T12:30",
                    "trainingDuration": 60
                }
                """.formatted(traineeUsername, trainerUsername);

        mockMvc.perform(post("/api/v1/trainings")
                        .header("X-Username", trainerUsername)
                        .header("X-Password", trainerPassword)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(trainingRequestJson))
                .andExpect(status().isOk());

        String trainingTraineeRequestJson = """
                {
                    "username": "%s",
                    "periodFrom": "2026-08-10T15:30:00",
                    "periodTo": "2026-08-30T15:30:00",
                    "trainerName": "%s",
                    "trainingType": "Python"
                }
                """.formatted(traineeUsername, trainerUsername);

        mockMvc.perform(get("/api/v1/trainings/trainee")
                        .header("X-Username", traineeUsername)
                        .header("X-Password", traineePassword)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(trainingTraineeRequestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].trainingName").value("Practice Python"))
                .andExpect(jsonPath("$[0].trainingType").exists())
                .andExpect(jsonPath("$[0].trainerName").exists());
    }

    @Test
    void testShouldGetTrainerTrainings() throws Exception {
        String specialization = "Python";
        var traineeRequest = new Trainee();
        traineeRequest.setFirstName("Casey");
        traineeRequest.setLastName("Sherman");
        var traineeResponse = traineeService.save(traineeRequest);
        String traineeUsername = traineeResponse.getUsername();

        var trainer = new Trainer();
        trainer.setFirstName("Jane");
        trainer.setLastName("Smith");
        var trainingType = trainingTypeService.findByName(specialization.toUpperCase());
        trainer.setSpecialization(trainingType);
        var trainerResponse = trainerService.save(trainer, specialization);
        String trainerUsername = trainerResponse.getUsername();
        String trainerPassword = trainerResponse.getPassword();

        String trainingRequestJson = """
                {
                    "traineeUsername": "%s",
                    "trainerUsername": "%s",
                    "trainingName": "Practice Python",
                    "trainingType": "Python",
                    "trainingDate": "2026-08-15T12:30",
                    "trainingDuration": 60
                }
                """.formatted(traineeUsername, trainerUsername);

        mockMvc.perform(post("/api/v1/trainings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(trainingRequestJson)
                        .header("X-Username", trainerUsername)
                        .header("X-Password", trainerPassword))
                .andExpect(status().isOk());

        String trainingTrainerRequestJson = """
                {
                    "username": "%s",
                    "periodFrom": "2026-08-10T15:30:00",
                    "periodTo": "2026-08-30T15:30:00",
                    "traineeName": "%s"
                }
                """.formatted(trainerUsername, traineeUsername);

        mockMvc.perform(get("/api/v1/trainings/trainer")
                        .header("X-Username", trainerUsername)
                        .header("X-Password", trainerPassword)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(trainingTrainerRequestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].trainingName").value("Practice Python"))
                .andExpect(jsonPath("$[0].trainingDuration").value(60));
    }
}
