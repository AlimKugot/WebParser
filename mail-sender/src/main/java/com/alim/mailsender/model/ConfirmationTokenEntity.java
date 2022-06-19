package com.alim.mailsender.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmationTokenEntity {

    @Id
    private String id;
    private String email;
    private String token;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private LocalDateTime confirmedAt;

    public ConfirmationTokenEntity(String email, String token, LocalDateTime createdAt, LocalDateTime expiresAt) {
        this.email = email;
        this.token = token;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }
}
