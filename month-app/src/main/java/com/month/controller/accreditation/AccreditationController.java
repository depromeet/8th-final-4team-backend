package com.month.controller.accreditation;

import com.month.config.resolver.LoginMember;
import com.month.controller.ApiResponse;
import com.month.service.accreditation.AccreditationService;
import com.month.service.accreditation.dto.request.AccreditationRequest;
import com.month.service.accreditation.dto.response.AccreditationResponse;
import com.month.type.session.MemberSession;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class AccreditationController {

    private final AccreditationService accreditationService;

    @ApiOperation("챌린지 인증을 저장하는 API")
    @PostMapping("/api/v1/accreditation/save")
    public ApiResponse<String> saveAccreditation(@LoginMember MemberSession memberSession,
                                                 AccreditationRequest request,
                                                 @RequestPart(value = "image", required = false) MultipartFile image) {
        if (image != null) request.setImage(image);
        accreditationService.saveAccreditation(memberSession.getMemberId(), request);
        return ApiResponse.OK;
    }

    @ApiOperation("날짜별 해당 챌린지의 인증 리스트를 불러오는 API")
    @GetMapping("/api/v1/accreditation")
    public ApiResponse<List<AccreditationResponse>> getAccreditationList(@LoginMember MemberSession memberSession,
                                                                         @RequestParam String challengeUuid,
                                                                         @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        return ApiResponse.of(accreditationService.getAccreditationList(memberSession.getMemberId(), challengeUuid, date));
    }

    @ApiOperation(("챌린지 인증을 할 수 있는지 확인하는 API"))
    @GetMapping("/api/v1/accreditation/check")
    public ApiResponse<String> getAccreditationCheck(@LoginMember MemberSession memberSession,
                                                     @RequestParam String challengeUuid) {
        accreditationService.getAccreditationCheck(memberSession.getMemberId(), challengeUuid);
        return ApiResponse.OK;
    }

}