package com.alim.mailsender.controller;

import com.alim.mailsender.dto.request.EmailRequest;
import com.alim.mailsender.model.UserEntity;
import com.alim.mailsender.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class HomeController {

    private final UserRepository userRepository;

    @GetMapping("/mails")
    public String getAll() {
        return userRepository.findAll().toString();
    }

    @PutMapping("/mails")
    public String put(@RequestBody UserEntity user) {
        userRepository.save(user);
        return "success";
    }
}
