package com.tradin.module.users.users.controller.dto.request;

import com.tradin.module.users.users.service.dto.PingDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PingRequestDto {
    @NotBlank(message = "Binance Api Key must not be blank")
    private String binanceApiKey;

    @NotBlank(message = "Binance Secret Key must not be blank")
    private String binanceSecretKey;

    public PingDto toServiceDto() {
        return PingDto.of(binanceApiKey, binanceSecretKey);
    }
}
