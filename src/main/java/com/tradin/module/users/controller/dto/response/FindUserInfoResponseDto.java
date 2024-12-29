package com.tradin.module.users.controller.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class FindUserInfoResponseDto {
    private final String name;
    private final String email;
}
