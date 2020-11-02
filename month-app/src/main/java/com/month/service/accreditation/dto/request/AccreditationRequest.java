package com.month.service.accreditation.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Setter
public class AccreditationRequest {

    private String challengeUuid;

    private String memo;

    private MultipartFile image;

}