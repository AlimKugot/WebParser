package com.alim.admin.service.impl;

import com.alim.admin.dto.request.UserRequestDto;
import com.alim.admin.exception.BusinessException;
import com.alim.admin.model.Role;
import com.alim.admin.model.UserEntity;
import com.alim.admin.repositories.UserRepository;
import com.alim.admin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserEntity save(UserRequestDto userRequestDto) {
        UserEntity admin = UserEntity.builder()
                .name(userRequestDto.getName())
                .email(userRequestDto.getEmail())
                .password(passwordEncoder.encode(userRequestDto.getPassword()))
                .role(Role.GUEST)
                .build();
        try {
            userRepository.save(admin);
            return admin;
        } catch (DataAccessException dataAccessException) {
            throw new BusinessException("Cannot save admin: " + dataAccessException.getMessage());
        }
    }
}
