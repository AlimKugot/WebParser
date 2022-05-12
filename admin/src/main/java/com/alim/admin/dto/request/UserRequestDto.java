package com.alim.admin.dto.request;

import lombok.Data;

@Data
public class UserRequestDto {
    private String name;
    private String email;
    private String password;
    private String repeatPassword;
}
