package andrehsvictor.kanshi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final String[] PUBLIC_ROUTES = {
            "/v3/api-docs/**",
            "/swagger-ui.html",
            "/swagger-ui/**"
    };

    private final String[] AUTHENTICATION_ROUTES = {
            "/api/1.0/authenticate",
            "/api/1.0/register"
    };

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(c -> c.requestMatchers(HttpMethod.POST, AUTHENTICATION_ROUTES).permitAll()
                        .requestMatchers(PUBLIC_ROUTES).permitAll()
                        .anyRequest().authenticated())
                .oauth2ResourceServer(c -> c.jwt(Customizer.withDefaults()))
                .build();
    }
}
