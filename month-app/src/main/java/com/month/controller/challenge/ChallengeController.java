package com.month.controller.challenge;

import com.month.config.resolver.LoginMember;
import com.month.controller.ApiResponse;
import com.month.service.challenge.ChallengeService;
import com.month.service.challenge.dto.request.ChallengeCreateInvitationKeyRequest;
import com.month.service.challenge.dto.request.ChallengeCreateRequest;
import com.month.service.challenge.dto.request.ChallengeInviteRequest;
import com.month.service.challenge.dto.request.ChallengeRetrieveRequest;
import com.month.service.challenge.dto.response.ChallengeInfoResponse;
import com.month.service.challenge.dto.response.ChallengeSimpleInfoResponse;
import com.month.type.session.MemberSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class ChallengeController {

	private final ChallengeService challengeService;

	@PostMapping("/api/v1/challenge")
	public ApiResponse<ChallengeSimpleInfoResponse> createNewChallenge(@Valid @RequestBody ChallengeCreateRequest request, @LoginMember MemberSession memberSession) {
		return ApiResponse.of(challengeService.createNewChallenge(request, memberSession.getMemberId()));
	}

	@GetMapping("/api/v1/challenge")
	public ApiResponse<ChallengeInfoResponse> getChallengeInfo(@Valid ChallengeRetrieveRequest request, @LoginMember MemberSession memberSession) {
		return ApiResponse.of(challengeService.getDetailChallengeInfo(request, memberSession.getMemberId()));
	}

	@GetMapping("/api/v1/challenge/my")
	public ApiResponse<List<ChallengeSimpleInfoResponse>> getMyChallengesInfo(@LoginMember MemberSession memberSession) {
		return ApiResponse.of(challengeService.getMyChallengeInfo(memberSession.getMemberId()));
	}

	@PutMapping("/api/v1/challenge/inviteKey")
	public ApiResponse<String> creatChallengeInviteKey(@Valid @RequestBody ChallengeCreateInvitationKeyRequest request, @LoginMember MemberSession memberSession) {
		return ApiResponse.of(challengeService.createInvitationKey(request, memberSession.getMemberId()));
	}

	@PutMapping("/api/v1/challenge/invite")
	public ApiResponse<ChallengeSimpleInfoResponse> inviteMemberByInvitationKey(@Valid @RequestBody ChallengeInviteRequest request, @LoginMember MemberSession memberSession) {
		return ApiResponse.of(challengeService.inviteNewMemberWithInvitationKey(request, memberSession.getMemberId()));
	}

	@GetMapping("/api/v1/challenge/invite")
	public ApiResponse<ChallengeSimpleInfoResponse> getInviteChallengeInfo(@Valid @RequestParam String invitationKey) {
		return ApiResponse.of(challengeService.getSimpleChallengeInfoByInvitationKey(invitationKey));
	}

}
