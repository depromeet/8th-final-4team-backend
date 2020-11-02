package com.month.controller.accreditation;

import com.month.config.resolver.LoginMember;
import com.month.controller.ApiResponse;
import com.month.service.accreditation.AccreditationService;
import com.month.service.accreditation.dto.request.AccreditationRequest;
import com.month.type.session.MemberSession;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
public class AccreditationController {

    private final AccreditationService accreditationService;

    @ApiOperation("챌린지 인증을 저장하는 API")
    @PostMapping("/api/v1/accreditation")
    public ApiResponse<String> saveAccreditation(@LoginMember MemberSession memberSession,
                                                 AccreditationRequest request,
                                                 @RequestPart(value = "image", required = false) MultipartFile image) {
        if (image != null) request.setImage(image);
        accreditationService.saveAccreditation(memberSession.getMemberId(), request);
        return ApiResponse.of("챌린지 인증 완료");
    }
}