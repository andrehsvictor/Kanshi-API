package andrehsvictor.kanshi.service;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import andrehsvictor.kanshi.dto.JwtDTO;

@Service
public class JwtService {

    @Autowired
    private JwtEncoder jwtEncoder;

    public JwtDTO generateJwt(Authentication authentication) {
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(authentication.getName())
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusMillis(3600000))
                .issuer("kanshi")
                .build();

        String token = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return new JwtDTO(claims.getSubject(), claims.getIssuedAt(), claims.getExpiresAt(), token);
    }
}
