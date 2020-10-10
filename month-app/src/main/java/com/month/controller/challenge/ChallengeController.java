package com.month.controller.challenge;

import com.month.config.resolver.LoginMember;
import com.month.controller.ApiResponse;
import com.month.service.challenge.ChallengeService;
import com.month.service.challenge.dto.request.CreateChallengePlanRequest;
import com.month.service.challenge.dto.request.EnterChallengeByInvitationKeyRequest;
import com.month.service.challenge.dto.request.RefreshChallengeInvitationKeyRequest;
import com.month.service.challenge.dto.request.RetrieveChallengePlanInvitationKeyRequest;
import com.month.service.challenge.dto.request.StartChallengeRequest;
import com.month.service.challenge.dto.response.ChallengeInfoResponse;
import com.month.service.challenge.dto.response.ChallengePlanInfoResponse;
import com.month.service.challenge.dto.response.ChallengePlanInvitationInfo;
import com.month.type.session.MemberSession;
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
public class ChallengeController {

	private final ChallengeService challengeService;

	/**
	 * 새로운 챌린지를 생성하는 APi
	 */
	@PostMapping("/api/v1/challenge")
	public ApiResponse<ChallengePlanInfoResponse> createChallengePlan(@Valid @RequestBody CreateChallengePlanRequest request, @LoginMember MemberSession memberSession) {
		return ApiResponse.of(challengeService.createChallengePlan(request, memberSession.getMemberId()));
	}

	/**
	 * 챌린지를 시작하는 API
	 */
	@PutMapping("/api/v1/challenge/start")
	public ApiResponse<ChallengeInfoResponse> startChallenge(@Valid @RequestBody StartChallengeRequest request, @LoginMember MemberSession memberSession) {
		return ApiResponse.of(challengeService.startChallenge(request, memberSession.getMemberId()));
	}

	/**
	 * 챌린지의 초대키를 반환하는 API
	 */
	@GetMapping("/api/v1/challenge/invitation-key")
	public ApiResponse<String> getChallengePlanInvitation(RetrieveChallengePlanInvitationKeyRequest request, @LoginMember MemberSession memberSession) {
		return ApiResponse.of(challengeService.getChallengePlanInvitationKey(request, memberSession.getMemberId()));
	}

	/**
	 * 챌린지의 초대키를 재발급 하는 API
	 */
	@PutMapping("/api/v1/challenge/invitation-key")
	public ApiResponse<String> refreshChallengeInvitationKey(@Valid @RequestBody RefreshChallengeInvitationKeyRequest request, @LoginMember MemberSession memberSession) {
		return ApiResponse.of(challengeService.refreshChallengeInvitationKey(request, memberSession.getMemberId()));
	}

	/**
	 * 챌린지의 초대키로 챌린지의 간단한 정보를 반환하는 API
	 */
	@GetMapping("/api/v1/challenge/invitation")
	public ApiResponse<ChallengePlanInvitationInfo> getChallengePlanInfoByInvitationKey(String invitationKey) {
		return ApiResponse.of(challengeService.getChallengeInfoByInvitationKey(invitationKey));
	}

	/**
	 * 초대키로 챌린지에 참여하는 API
	 */
	@PutMapping("/api/v1/challenge/enter")
	public ApiResponse<String> enterChallengeByInvitationKey(@Valid @RequestBody EnterChallengeByInvitationKeyRequest request, @LoginMember MemberSession memberSession) {
		challengeService.enterChallengeByInvitationKey(request, memberSession.getMemberId());
		return ApiResponse.OK;
	}

	/**
	 * 내가 참여하고 있는 챌린지의 리스트를 불러오는 API
	 */
	@GetMapping("/api/v1/challenges/my")
	public ApiResponse<List<ChallengeInfoResponse>> retrieveMyChallenges(@LoginMember MemberSession memberSession) {
		return ApiResponse.of(challengeService.retrieveMyChallengeList(memberSession.getMemberId()));
	}

}
