package andrehsvictor.kanshi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import andrehsvictor.kanshi.dto.AuthenticateDTO;
import andrehsvictor.kanshi.dto.JwtDTO;
import andrehsvictor.kanshi.service.AuthenticationService;

@RestController
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/api/1.0/authenticate")
    public ResponseEntity<JwtDTO> authenticate(@RequestBody AuthenticateDTO authenticateDTO) {
        return ResponseEntity.ok(authenticationService.authenticate(authenticateDTO));
    }
}
