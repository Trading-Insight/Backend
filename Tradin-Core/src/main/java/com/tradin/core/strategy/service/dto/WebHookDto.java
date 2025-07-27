package com.tradin.core.strategy.service.dto;

import com.tradin.core.strategy.domain.Position;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class WebHookDto {

    private final Long id;
    private final Position position;

    public static WebHookDto of(Long id, Position position) {
        return new WebHookDto(id, position);
    }
}
