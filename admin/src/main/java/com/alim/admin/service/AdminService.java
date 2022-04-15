package com.alim.admin.service;

import com.alim.admin.model.AdminEntity;
import org.springframework.util.MultiValueMap;

public interface AdminService {

    AdminEntity save(MultiValueMap<String, String> body);
}
