package com.month.controller.challenge;

import com.month.config.resolver.LoginMember;
import com.month.controller.ApiResponse;
import com.month.service.challenge.ChallengeService;
import com.month.service.challenge.dto.request.CreateNewChallengeRequest;
import com.month.service.challenge.dto.request.GetChallengeInfoByInvitationKeyRequest;
import com.month.service.challenge.dto.request.GetInvitationKeyRequest;
import com.month.service.challenge.dto.response.ChallengeResponse;
import com.month.service.challenge.dto.response.MyChallengesResponse;
import com.month.type.session.MemberSession;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class ChallengeController {

	private final ChallengeService challengeService;

	@ApiOperation("새로운 챌린지를 생성하는 API")
	@ApiImplicitParam(name = "Authorization", value = "Bearer Token", required = true, paramType = "header")
	@PostMapping("/api/v1/challenge")
	public ApiResponse<ChallengeResponse> createNewChallenge(@Valid @RequestBody CreateNewChallengeRequest request, @LoginMember MemberSession memberSession) {
		return ApiResponse.of(challengeService.createNewChallenge(request, memberSession.getMemberId()));
	}

	@ApiOperation("나의 챌린지 리스트를 조회하는 API")
	@ApiImplicitParam(name = "Authorization", value = "Bearer Token", required = true, paramType = "header")
	@GetMapping("/api/v1/challenge/list")
	public ApiResponse<MyChallengesResponse> retrieveMyChallengeList(@LoginMember MemberSession memberSession) {
		return ApiResponse.of(challengeService.retrieveMyChallenges(memberSession.getMemberId()));
	}

	@ApiOperation("챌린지의 초대키를 발급받는 API")
	@ApiImplicitParam(name = "Authorization", value = "Bearer Token", required = true, paramType = "header")
	@GetMapping("/api/v1/challenge/invite/key")
	public ApiResponse<String> getInvitationKey(@Valid GetInvitationKeyRequest request, @LoginMember MemberSession memberSession) {
		return ApiResponse.of(challengeService.getInvitationKey(request, memberSession.getMemberId()));
	}

	@ApiOperation("초대키로 챌린지의 정보를 조회하는 API")
	@GetMapping("/api/v1/challenge/invite")
	public ApiResponse<ChallengeResponse> getChallengeInfoByInvitationKey(@Valid GetChallengeInfoByInvitationKeyRequest request) {
		return ApiResponse.of(challengeService.getChallengeInfoByInvitationKey(request));
	}

	@ApiOperation("내가 초대받은 챌린지의 리스트를 조회하는 API")
	@ApiImplicitParam(name = "Authorization", value = "Bearer Token", required = true, paramType = "header")
	@GetMapping("/api/v1/challenge/invite/list")
	public ApiResponse<List<ChallengeResponse>> retrieveInvitedChallengeList(@LoginMember MemberSession memberSession) {
		return ApiResponse.of(challengeService.retrieveInvitedChallengeList(memberSession.getMemberId()));
	}

	@ApiOperation("초대키로 챌린지에 참가하는 API")
	@ApiImplicitParam(name = "Authorization", value = "Bearer Token", required = true, paramType = "header")
	@PutMapping("/api/v1/challenge/invite")
	public ApiResponse<String> participateByInvitationKey(@Valid @RequestBody String invitationKey, @LoginMember MemberSession memberSession) {
		challengeService.participateByInvitationKey(invitationKey, memberSession.getMemberId());
		return ApiResponse.OK;
	}

}
