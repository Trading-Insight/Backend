package com.tradin.core.users.service.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class FindUserInfoResponseDto {
    private final String name;
    private final String email;
}
