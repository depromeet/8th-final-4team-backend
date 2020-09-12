package com.month.controller.challenge;

import com.month.config.resolver.LoginMember;
import com.month.controller.ApiResponse;
import com.month.service.challenge.ChallengeService;
import com.month.service.challenge.dto.request.ChallengeCreateRequest;
import com.month.service.challenge.dto.request.ChallengeRetrieveRequest;
import com.month.service.challenge.dto.response.ChallengeInfoResponse;
import com.month.type.session.MemberSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class ChallengeController {

	private final ChallengeService challengeService;

	@PostMapping("/api/v1/challenge")
	public ApiResponse<ChallengeInfoResponse> createNewChallenge(@Valid @RequestBody ChallengeCreateRequest request, @LoginMember MemberSession memberSession) {
		return ApiResponse.of(challengeService.createNewChallenge(request, memberSession.getMemberId()));
	}

	@GetMapping("/api/v1/challenge")
	public ApiResponse<ChallengeInfoResponse> getChallengeInfo(@Valid ChallengeRetrieveRequest request, @LoginMember MemberSession memberSession) {
		return ApiResponse.of(challengeService.getChallengeInfo(request, memberSession.getMemberId()));
	}

}
