package com.alim.admin.dto.request;

import lombok.Data;
import org.springframework.util.MultiValueMap;


@Data
public class AdminRequestDto {
    private MultiValueMap<String, String> body;
}
