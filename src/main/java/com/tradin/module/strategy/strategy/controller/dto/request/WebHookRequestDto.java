package com.tradin.module.strategy.strategy.controller.dto.request;

import com.tradin.module.strategy.strategy.domain.Position;
import com.tradin.module.strategy.strategy.service.dto.WebHookDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class WebHookRequestDto {
    @NotBlank(message = "name must not be blank")
    private String name;

    @NotBlank(message = "position must not be blank")
    private Position position;

    public WebHookDto toServiceDto() {
        return WebHookDto.of(name, position);
    }
}
