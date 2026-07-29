package epam.controller.interfaces;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface LoginController {

    @GetMapping("/trainee/login")
    @Operation(summary = "Login", description = "Login a user by username and password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login executed successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    ResponseEntity<Void> loginTrainee(
            @Parameter(description = "User username", required = true)
            @RequestParam("username") String username,
            @Parameter(description = "User password", required = true)
            @RequestParam("password") String password);

    @GetMapping("/trainer/login")
    @Operation(summary = "Login", description = "Login a user by username and password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login executed successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    ResponseEntity<Void> loginTrainer(
            @Parameter(description = "User username", required = true)
            @RequestParam("username") String username,
            @Parameter(description = "User password", required = true)
            @RequestParam("password") String password);
}
