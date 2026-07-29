package epam.controller.rest;

import epam.application.FacadeGymCrmSystem;
import epam.controller.interfaces.LoginController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class LoginControllerImpl implements LoginController {

    private final FacadeGymCrmSystem facadeGymCrmSystem;

    @Override
    public ResponseEntity<Void> loginTrainee(String username, String password) {
        log.info("Login for user with username: {}", username);

        facadeGymCrmSystem.loginTrainee(username, password);

        log.info("Credentials were successfully performed for user: {}", username);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> loginTrainer(String username, String password) {
        log.info("Login for user with username: {}", username);

        facadeGymCrmSystem.loginTrainer(username, password);

        log.info("Credentials were successfully performed for user: {}", username);
        return ResponseEntity.ok().build();
    }
}
