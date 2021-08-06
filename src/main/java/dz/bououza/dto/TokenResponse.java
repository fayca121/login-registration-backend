package dz.bououza.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TokenResponse {
    private String email;
    private String token;
    private LocalDateTime createdAt;
}
