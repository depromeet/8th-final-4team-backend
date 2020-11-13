package com.month.controller.challenge;

import com.month.config.resolver.LoginMember;
import com.month.controller.ApiResponse;
import com.month.service.challenge.ChallengePlanService;
import com.month.service.challenge.dto.request.*;
import com.month.service.challenge.dto.response.ChallengePlanInfoResponse;
import com.month.service.challenge.dto.response.ChallengePlanInvitationInfo;
import com.month.type.session.MemberSession;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class ChallengePlanController {

	private final ChallengePlanService challengePlanService;

	@ApiOperation("새로운 계획중인 챌린지를 생성하는 APi")
	@ApiImplicitParam(name = "Authorization", value = "Bearer Token", required = true, paramType = "header")
	@PostMapping("/api/v1/challenge")
	public ApiResponse<ChallengePlanInfoResponse> createChallengePlan(@Valid @RequestBody CreateChallengePlanRequest request, @LoginMember MemberSession memberSession) {
		return ApiResponse.of(challengePlanService.createChallengePlan(request, memberSession.getMemberId()));
	}

	@ApiOperation("아직 시작하지 않은 계획중인 챌린지 리스트를 반환하는 API")
	@ApiImplicitParam(name = "Authorization", value = "Bearer Token", required = true, paramType = "header")
	@GetMapping("/api/v1/challenge/plan")
	public ApiResponse<List<ChallengePlanInfoResponse>> retrieveChallengePlans(@LoginMember MemberSession memberSession) {
		return ApiResponse.of(challengePlanService.retrieveMyChallengePlans(memberSession.getMemberId()));
	}

	@ApiOperation("계획중인 챌린지의 초대키를 반환하는 API")
	@ApiImplicitParam(name = "Authorization", value = "Bearer Token", required = true, paramType = "header")
	@GetMapping("/api/v1/challenge/invitation-key")
	public ApiResponse<String> getChallengePlanInvitation(@Valid RetrieveChallengePlanInvitationKeyRequest request, @LoginMember MemberSession memberSession) {
		return ApiResponse.of(challengePlanService.getChallengePlanInvitationKey(request, memberSession.getMemberId()));
	}

	@ApiOperation("계획중인 챌린지의 초대키를 재발급 하는 API")
	@ApiImplicitParam(name = "Authorization", value = "Bearer Token", required = true, paramType = "header")
	@PutMapping("/api/v1/challenge/invitation-key")
	public ApiResponse<String> refreshChallengeInvitationKey(@Valid @RequestBody RefreshChallengeInvitationKeyRequest request, @LoginMember MemberSession memberSession) {
		return ApiResponse.of(challengePlanService.refreshChallengeInvitationKey(request, memberSession.getMemberId()));
	}

	@ApiOperation("챌린지의 초대키로 계획중인 챌린지의 간단한 정보를 반환하는 API")
	@GetMapping("/api/v1/challenge/invitation")
	public ApiResponse<ChallengePlanInvitationInfo> getChallengePlanInfoByInvitationKey(@Valid RetrieveChallengePlanByInvitationKeyRequest request) {
		return ApiResponse.of(challengePlanService.getChallengeInfoByInvitationKey(request.getInvitationKey()));
	}

	@ApiOperation("초대키로 계획중인 챌린지에 참여하는 API")
	@ApiImplicitParam(name = "Authorization", value = "Bearer Token", required = true, paramType = "header")
	@PutMapping("/api/v1/challenge/enter")
	public ApiResponse<String> enterChallengeByInvitationKey(@Valid @RequestBody EnterChallengeByInvitationKeyRequest request, @LoginMember MemberSession memberSession) {
		challengePlanService.enterChallengeByInvitationKey(request, memberSession.getMemberId());
		return ApiResponse.OK;
	}

}