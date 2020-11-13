package com.month.controller.challenge;

import com.month.config.resolver.LoginMember;
import com.month.controller.ApiResponse;
import com.month.service.challenge.ChallengeService;
import com.month.service.challenge.dto.request.StartChallengeRequest;
import com.month.service.challenge.dto.response.ChallengeInfoResponse;
import com.month.type.session.MemberSession;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class ChallengeController {

	private final ChallengeService challengeService;

	@ApiOperation("아직 시작하지 않은 계획중인 챌린지를 시작하는 API")
	@ApiImplicitParam(name = "Authorization", value = "Bearer Token", required = true, paramType = "header")
	@PutMapping("/api/v1/challenge/start")
	public ApiResponse<ChallengeInfoResponse> startChallenge(@Valid @RequestBody StartChallengeRequest request, @LoginMember MemberSession memberSession) {
		return ApiResponse.of(challengeService.startChallenge(request, memberSession.getMemberId()));
	}

	@ApiOperation("내가 참여하고 있는 챌린지의 리스트를 불러오는 API")
	@ApiImplicitParam(name = "Authorization", value = "Bearer Token", required = true, paramType = "header")
	@GetMapping("/api/v1/challenges/my")
	public ApiResponse<List<ChallengeInfoResponse>> retrieveMyChallenges(@LoginMember MemberSession memberSession) {
		return ApiResponse.of(challengeService.retrieveMyChallengeList(memberSession.getMemberId()));
	}

}
