package epam.controller.rest;

import epam.application.FacadeGymCrmSystem;
import epam.controller.interfaces.TraineeController;
import epam.domain.dto.request.ChangePasswordRequestDTO;
import epam.domain.dto.request.TraineeRequestDTO;
import epam.domain.dto.request.UpdateTraineeRequestDTO;
import epam.domain.dto.request.UpdateTraineeTrainersRequestDTO;
import epam.domain.dto.response.RegistrationResponseDTO;
import epam.domain.dto.response.TraineeProfileDTO;
import epam.domain.dto.response.TrainerInfoDTO;
import epam.service.TraineeService;
import epam.service.TrainerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/trainees")
@RequiredArgsConstructor
public class TraineeControllerImpl implements TraineeController {

    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final FacadeGymCrmSystem facadeGymCrmSystem;

    // http://localhost:8080/swagger-ui.html

    @Override
    public ResponseEntity<RegistrationResponseDTO> registerTrainee(TraineeRequestDTO request) {
        log.info("Register trainee request received: {} {}", request.getFirstName(), request.getLastName());

        RegistrationResponseDTO response = facadeGymCrmSystem.createTrainee(request);

        log.info("Trainee registered successfully: {}", response.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @Override
    public ResponseEntity<TraineeProfileDTO> getTraineeProfile(String username,
                                                               String headerUsername,
                                                               String headerPassword) {
        log.info("Get trainee profile request received for username: {}", username);

        TraineeProfileDTO profile = facadeGymCrmSystem.getTraineeByUsername(username, headerUsername,
                                                                            headerPassword);
        log.info("Trainee profile retrieved successfully for username: {}", username);
        return ResponseEntity.ok(profile);
    }

    @Override
    public ResponseEntity<Void> deleteTraineeProfile(String username, String headerUsername,
                                                     String headerPassword) {
        log.info("Delete trainee profile request received for username: {}", username);

        facadeGymCrmSystem.deleteTrainee(username, headerUsername, headerPassword);

        log.info("Trainee profile deleted successfully for username: {}", username);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<List<TrainerInfoDTO>> getAvailableTrainers(String username, String headerUsername,
                                                                     String headerPassword) {
        log.info("Get available trainers request received for trainee: {}", username);

        List<TrainerInfoDTO> trainers = facadeGymCrmSystem.getAvailableTrainers(username,
                headerUsername, headerPassword);

        log.info("Found {} available trainers for trainee: {}", trainers.size(), username);
        return ResponseEntity.ok(trainers);
    }

    @Override
    public ResponseEntity<TraineeProfileDTO> updateTraineeProfile(UpdateTraineeRequestDTO traineeRequestDTO,
                                                                  String headerUsername,
                                                                  String headerPassword) {
        log.info("Update trainee profile request received for username: {}", traineeRequestDTO.getUsername());

        TraineeProfileDTO profile = facadeGymCrmSystem.updateTraineeProfile(traineeRequestDTO,
                headerUsername, headerPassword);

        log.info("Trainee profile updated successfully for username: {}", traineeRequestDTO.getUsername());
        return ResponseEntity.ok(profile);
    }

    @Override // 4
    public ResponseEntity<Void> changePassword(ChangePasswordRequestDTO request, String headerUsername,
                                               String headerPassword) {
        log.info("Change password request received for trainee: {}", request.getUsername());

        facadeGymCrmSystem.changeTraineePassword(request, headerUsername, headerPassword);

        log.info("Password changed successfully for trainee: {}", request.getUsername());
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> activateDeactivateTrainee(String username, Boolean isActive, String headerUsername,
                                                          String headerPassword) {
        log.info("Activate/Deactivate trainee request received for username: {}, isActive: {}", username, isActive);

        facadeGymCrmSystem.activateDeactivateTrainee(username, isActive, headerUsername, headerPassword);

        log.info("Trainee status updated successfully for username: {}, new status: {}", username, isActive);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<List<TrainerInfoDTO>> updateTrainersList(UpdateTraineeTrainersRequestDTO request,
                                                                   String headerUsername,
                                                                   String headerPassword) {
        log.info("Update trainers list request received for trainee: {}", request.getTraineeUsername());

        List<TrainerInfoDTO> trainers = facadeGymCrmSystem.updateTrainersList(request, headerUsername, headerPassword);

        log.info("Trainers list updated successfully for trainee: {}, trainers count: {}", request.getTraineeUsername(),
                trainers.size());
        return ResponseEntity.ok(trainers);
    }
}
