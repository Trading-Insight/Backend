package com.tradin.module.strategy.service.dto;

import com.tradin.module.strategy.domain.Position;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class WebHookDto {
    private final String name;
    private final Position position;

    public static WebHookDto of(String name, Position position) {
        return new WebHookDto(name, position);
    }
}
