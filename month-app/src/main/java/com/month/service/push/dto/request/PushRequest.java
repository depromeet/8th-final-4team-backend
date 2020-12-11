package com.month.service.push.dto.request;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@NoArgsConstructor
public class PushRequest {

    @NotEmpty
    private List<String> deviceTokens;

    @NotBlank
    private String title;

    @NotBlank
    private String body;

    public static PushRequest of(List<String> deviceTokens, String title, String body) {
        PushRequest pushRequest = new PushRequest();
        pushRequest.deviceTokens = deviceTokens;
        pushRequest.title = title;
        pushRequest.body = body;
        return pushRequest;
    }
}