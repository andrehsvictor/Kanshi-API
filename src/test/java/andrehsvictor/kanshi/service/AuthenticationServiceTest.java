package andrehsvictor.kanshi.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import java.time.Instant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;

import andrehsvictor.kanshi.dto.AuthenticateDTO;
import andrehsvictor.kanshi.dto.JwtDTO;

class AuthenticationServiceTest {

    @Mock
    JwtService jwtService;

    @Mock
    AuthenticationManager authenticationManager;

    @Autowired
    @InjectMocks
    AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void authenticate_shouldReturnJwtDTO() {
        JwtDTO jwtDTO = new JwtDTO("subject", Instant.now(), Instant.now().plusSeconds(3600), "token");
        Authentication authentication = mock(Authentication.class);
        AuthenticateDTO authenticateDTO = new AuthenticateDTO("email", "password");

        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(jwtService.generateJwt(any())).thenReturn(jwtDTO);

        JwtDTO result = authenticationService.authenticate(authenticateDTO);

        assertThat(result).isEqualTo(jwtDTO);
        
        verify(authenticationManager).authenticate(any());
        verify(jwtService).generateJwt(any());
    }

    @Test
    void authenticate_shouldThrowException_whenAuthenticationManagerThrowsException() {
        AuthenticateDTO authenticateDTO = new AuthenticateDTO("email", "password");

        when(authenticationManager.authenticate(any())).thenThrow(new RuntimeException());

        try {
            authenticationService.authenticate(authenticateDTO);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(RuntimeException.class);
        }
    }

    @Test
    void authenticate_shouldThrowException_whenJwtServiceThrowsException() {
        Authentication authentication = mock(Authentication.class);
        AuthenticateDTO authenticateDTO = new AuthenticateDTO("email", "password");

        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(jwtService.generateJwt(any())).thenThrow(new RuntimeException());

        try {
            authenticationService.authenticate(authenticateDTO);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(RuntimeException.class);
        }
    }
}
