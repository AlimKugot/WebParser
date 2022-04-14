package com.alim.admin.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Admin {

    private String name;
    private String role;
    private String email;
    private String password;
    private LocalDateTime registrationDate;
}
