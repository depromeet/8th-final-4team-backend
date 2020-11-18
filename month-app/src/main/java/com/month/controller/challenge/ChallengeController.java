package com.month.controller.challenge;

import com.month.config.resolver.LoginMember;
import com.month.controller.ApiResponse;
import com.month.service.challenge.ChallengeService;
import com.month.service.challenge.dto.request.CreateNewChallengeRequest;
import com.month.service.challenge.dto.response.ChallengeResponse;
import com.month.service.challenge.dto.response.MyChallengesResponse;
import com.month.type.session.MemberSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class ChallengeController {

	private final ChallengeService challengeService;

	@PostMapping("/api/v1/challenge")
	public ApiResponse<ChallengeResponse> createNewChallenge(@Valid @RequestBody CreateNewChallengeRequest request, @LoginMember MemberSession memberSession) {
		return ApiResponse.of(challengeService.createNewChallenge(request, memberSession.getMemberId()));
	}

	@GetMapping("/api/v1/challenge/list")
	public ApiResponse<MyChallengesResponse> retrieveMyChallengeList(@LoginMember MemberSession memberSession) {
		return ApiResponse.of(challengeService.retrieveMyChallenges(memberSession.getMemberId()));
	}

}
