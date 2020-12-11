package com.month.service.member.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class DeviceTokenRequest {

    @NotBlank
    private String deviceToken;

}
