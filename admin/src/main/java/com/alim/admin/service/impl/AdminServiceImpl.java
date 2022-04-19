package com.alim.admin.service.impl;

import com.alim.admin.exception.BusinessException;
import com.alim.admin.model.AdminEntity;
import com.alim.admin.repositories.AdminRepository;
import com.alim.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;


@Service("adminService")
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AdminEntity save(MultiValueMap<String, String> body) {
        AdminEntity admin = AdminEntity.builder()
                .name(body.getFirst("name"))
                .email(body.getFirst("email"))
                .password(passwordEncoder.encode(body.getFirst("password")))
//   todo:             .role()
                .build();
        try {
            adminRepository.save(admin);
            return admin;
        } catch (DataAccessException dataAccessException) {
            throw new BusinessException("Cannot save admin: " + dataAccessException.getMessage());
        }
    }
}
