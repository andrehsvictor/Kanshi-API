package andrehsvictor.kanshi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import andrehsvictor.kanshi.dto.AuthenticateDTO;
import andrehsvictor.kanshi.dto.JwtDTO;

@Service
public class AuthenticationService {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public JwtDTO authenticate(AuthenticateDTO authenticateDTO) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                authenticateDTO.getEmail(), authenticateDTO.getPassword());
        try {
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            return jwtService.generateJwt(authentication);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
    }
}
