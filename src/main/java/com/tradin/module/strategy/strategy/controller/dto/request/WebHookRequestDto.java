package com.tradin.module.strategy.strategy.controller.dto.request;

import com.tradin.module.strategy.strategy.domain.Position;
import com.tradin.module.strategy.strategy.service.dto.WebHookDto;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class WebHookRequestDto {

    @NotNull(message = "id must not be blank")
    private Long id;

    @NotNull(message = "position must not be null")
    private Position position;

    public WebHookDto toServiceDto() {
        return WebHookDto.of(id, position);
    }
}
