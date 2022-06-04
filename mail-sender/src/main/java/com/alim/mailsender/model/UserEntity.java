package com.alim.mailsender.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@AllArgsConstructor
@Data
public class UserEntity {

    @Id
    private String id;
    private String email;
    private String name;

}
