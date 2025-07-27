package com.tradin.api.users.dto;

import com.tradin.core.users.service.dto.PingDto;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
