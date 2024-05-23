package andrehsvictor.kanshi.dto;

import java.time.Instant;

public class JwtDTO {
    private String subject;
    private Instant issuedAt;
    private Instant expiresAt;
    private String token;

    public JwtDTO() {
    }

    public JwtDTO(String subject, Instant issuedAt, Instant expiresAt, String token) {
        this.subject = subject;
        this.issuedAt = issuedAt;
        this.expiresAt = expiresAt;
        this.token = token;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Instant getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(Instant issuedAt) {
        this.issuedAt = issuedAt;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    
}
