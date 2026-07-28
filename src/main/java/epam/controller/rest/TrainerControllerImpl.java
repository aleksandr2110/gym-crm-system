package epam.controller.rest;

import epam.application.FacadeGymCrmSystem;
import epam.controller.interfaces.TrainerController;
import epam.domain.dto.request.ChangePasswordRequestDTO;
import epam.domain.dto.request.TrainerRequestDTO;
import epam.domain.dto.request.UpdateTrainerRequestDTO;
import epam.domain.dto.response.RegistrationResponseDTO;
import epam.domain.dto.response.TrainerProfileDTO;
import epam.service.TrainerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/trainers")
@RequiredArgsConstructor
public class TrainerControllerImpl implements TrainerController {

    private final TrainerService trainerService;
    private final FacadeGymCrmSystem facadeGymCrmSystem;
    // http://localhost:8080/swagger-ui.html

    @Override
    public ResponseEntity<RegistrationResponseDTO> registerTrainer(TrainerRequestDTO request) {
        log.info("Register trainer request received: {} {}", request.getFirstName(), request.getLastName());

        RegistrationResponseDTO response = facadeGymCrmSystem.createTrainer(request);

        log.info("Trainer registered successfully: {}", response.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public ResponseEntity<TrainerProfileDTO> getTrainerProfile(String username,
                                                               String headerUsername,
                                                               String headerPassword) {
        log.info("Get trainer profile request received for username: {}", username);

        trainerService.authenticateTrainer(headerUsername, headerPassword);
        TrainerProfileDTO profile = trainerService.findByUsername(username);

        log.info("Trainer profile retrieved successfully for username: {}", username);
        return ResponseEntity.ok(profile);
    }

    @Override
    public ResponseEntity<TrainerProfileDTO> updateTrainerProfile(UpdateTrainerRequestDTO request,
                                                                  String headerUsername,
                                                                  String headerPassword) {
        log.info("Update trainer profile request received for username: {}", request.getUsername());

        trainerService.authenticateTrainer(headerUsername, headerPassword);
        TrainerProfileDTO profile = trainerService.updateProfile(request);

        log.info("Trainer profile updated successfully for username: {}", request.getUsername());
        return ResponseEntity.ok(profile);
    }

    @Override
    public ResponseEntity<Void> changePassword(ChangePasswordRequestDTO request,
                                               String headerUsername,
                                               String headerPassword) {
        log.info("Change password request received for trainer: {}", request.getUsername());

        trainerService.authenticateTrainer(headerUsername, headerPassword);
        trainerService.changePassword(request.getUsername(), request.getOldPassword(), request.getNewPassword());

        log.info("Password changed successfully for trainer: {}", request.getUsername());
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> activateDeactivateTrainer(String username, Boolean isActive,
                                                          String headerUsername, String headerPassword) {
        log.info("Activate/Deactivate trainer request received for username: {}, isActive: {}", username, isActive);

        trainerService.authenticateTrainer(headerUsername, headerPassword);
        trainerService.activateDeactivateTrainee(username, isActive);

        log.info("Trainer status updated successfully for username: {}, new status: {}", username, isActive);
        return ResponseEntity.ok().build();
    }
}
