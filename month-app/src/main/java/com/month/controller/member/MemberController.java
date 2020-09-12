package com.month.controller.member;

import com.month.config.resolver.LoginMember;
import com.month.controller.ApiResponse;
import com.month.service.member.MemberService;
import com.month.service.member.dto.request.MemberUpdateInfoRequest;
import com.month.service.member.dto.response.MemberInfoResponse;
import com.month.type.MemberSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class MemberController {

	private final MemberService memberService;

	@GetMapping("/api/v1/member")
	public ApiResponse<MemberInfoResponse> getMemberInfo(@LoginMember MemberSession memberSession) {
		return ApiResponse.of(memberService.getMemberInfo(memberSession.getMemberId()));
	}

	@PutMapping("/api/v1/member")
	public ApiResponse<MemberInfoResponse> updateMemberInfo(@Valid @RequestBody MemberUpdateInfoRequest request, @LoginMember MemberSession memberSession) {
		return ApiResponse.of(memberService.updateMemberInfo(request, memberSession.getMemberId()));
	}

}
