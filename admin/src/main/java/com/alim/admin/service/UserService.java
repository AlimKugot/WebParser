package com.alim.admin.service;

import com.alim.admin.dto.request.UserRequestDto;
import com.alim.admin.model.UserEntity;

public interface UserService {

    UserEntity save(UserRequestDto requestDto);
}
