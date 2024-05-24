package andrehsvictor.kanshi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import andrehsvictor.kanshi.dto.AuthenticateDTO;
import andrehsvictor.kanshi.dto.JwtDTO;
import andrehsvictor.kanshi.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Authentication")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/api/1.0/authenticate")
    @Operation(summary = "Authenticate user", responses = {
            @ApiResponse(responseCode = "200", description = "User authenticated successfully"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    public ResponseEntity<JwtDTO> authenticate(@RequestBody AuthenticateDTO authenticateDTO) {
        return ResponseEntity.ok(authenticationService.authenticate(authenticateDTO));
    }
}
