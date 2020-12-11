package com.month.controller.member;

import com.month.config.resolver.LoginMember;
import com.month.controller.ApiResponse;
import com.month.service.member.dto.request.DeviceTokenRequest;
import com.month.service.member.MemberService;
import com.month.service.member.dto.request.UpdateMemberInfoRequest;
import com.month.service.member.dto.response.MemberDetailInfoResponse;
import com.month.service.member.dto.response.MemberInfoResponse;
import com.month.type.session.MemberSession;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

    @ApiOperation("자신의 회원 정보를 불러오는 API")
    @ApiImplicitParam(name = "Authorization", value = "Bearer Token", required = true, paramType = "header")
    @GetMapping("/api/v1/member")
    public ApiResponse<MemberDetailInfoResponse> getMemberInfo(@LoginMember MemberSession memberSession) {
        return ApiResponse.of(memberService.getMemberInfo(memberSession.getMemberId()));
    }

    @ApiOperation("자신의 회원 정보를 수정하는 API")
    @ApiImplicitParam(name = "Authorization", value = "Bearer Token", required = true, paramType = "header")
    @PutMapping("/api/v1/member")
    public ApiResponse<MemberInfoResponse> updateMemberInfo(@Valid @RequestBody UpdateMemberInfoRequest request, @LoginMember MemberSession memberSession) {
        return ApiResponse.of(memberService.updateMemberInfo(request, memberSession.getMemberId()));
    }

    @ApiOperation("device token을 요청하는 API")
    @ApiImplicitParam(name = "Authorization", value = "Bearer Token", required = true, paramType = "header")
    @PostMapping("/api/v1/member/deviceToken")
    public ApiResponse<String> deviceTokenMember(@Valid @RequestBody DeviceTokenRequest request, @LoginMember MemberSession memberSession) {
        memberService.deviceTokenMember(request, memberSession.getMemberId());
        return ApiResponse.OK;
    }

}
