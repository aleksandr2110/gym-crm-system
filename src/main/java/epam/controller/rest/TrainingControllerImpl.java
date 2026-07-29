package epam.controller.rest;

import epam.application.FacadeGymCrmSystem;
import epam.controller.interfaces.TrainingController;
import epam.domain.dto.request.TraineeTrainingsRequestDTO;
import epam.domain.dto.request.TrainerTrainingsRequestDTO;
import epam.domain.dto.request.TrainingRequestDTO;
import epam.domain.dto.response.TrainingDTO;
import epam.domain.dto.response.TrainingTypeDTO;
import epam.service.TrainerService;
import epam.service.TrainingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/trainings")
@RequiredArgsConstructor
public class TrainingControllerImpl implements TrainingController {

    private final TrainingService trainingService;
    private final TrainerService trainerService;
    private final FacadeGymCrmSystem facadeGymCrmSystem;

    // http://localhost:8080/swagger-ui.html

    @Override
    public ResponseEntity<List<TrainingTypeDTO>> getTrainingTypes() {
        log.info("Get training types request received for username");

        List<TrainingTypeDTO> types = facadeGymCrmSystem.getTrainingTypes();

        log.info("Training types retrieved successfully!");
        return ResponseEntity.ok(types);
    }

    @Override
    public ResponseEntity<Void> addTraining(TrainingRequestDTO request,  String headerUsername,
                                            String headerPassword) {
        log.info("Add training request received: trainee={}, trainer={}, name={}",
                request.getTraineeUsername(), request.getTrainerUsername(), request.getTrainingName());

        facadeGymCrmSystem.createTraining(request, headerUsername, headerPassword);

        log.info("Training created successfully: trainee={}, trainer={}, name={}",
                request.getTraineeUsername(), request.getTrainerUsername(), request.getTrainingName());
        return ResponseEntity.ok().build();
    }

    @Override // 12
    public ResponseEntity<List<TrainingDTO>> getTraineeTrainings(TraineeTrainingsRequestDTO filterRequest,
                                                                 String headerUsername,
                                                                 String headerPassword) {
        log.info("Get trainee trainings request received for username: {}, filters: from={}, to={}, trainer={}, type={}",
                filterRequest.getUsername(), filterRequest.getPeriodFrom(), filterRequest.getPeriodTo(),
                filterRequest.getTrainerName(), filterRequest.getTrainingType());

        List<TrainingDTO> trainings = facadeGymCrmSystem.getTraineeTraining(filterRequest,
                headerUsername, headerPassword);

        log.info("Trainee trainings retrieved successfully for username: {}, count: {}",
                filterRequest.getUsername(), trainings.size());

        return ResponseEntity.ok(trainings);
    }

    @Override // 13
    public ResponseEntity<List<TrainingDTO>> getTrainerTrainings(TrainerTrainingsRequestDTO filterRequest,
                                                                 String headerUsername,
                                                                 String headerPassword) {
        log.info("Get trainer trainings request received for username: {}, filters: from={}, to={}, trainee={}",
                filterRequest.getUsername(), filterRequest.getPeriodFrom(), filterRequest.getPeriodTo(),
                filterRequest.getTraineeName());

        trainerService.authenticateTrainer(headerUsername, headerPassword);
        List<TrainingDTO> trainings = trainingService.selectTrainerTrainings(filterRequest);

        log.info("Trainer trainings retrieved successfully for username: {}, count: {}",
                filterRequest.getUsername(), trainings.size());
        return ResponseEntity.ok(trainings);
    }
}
