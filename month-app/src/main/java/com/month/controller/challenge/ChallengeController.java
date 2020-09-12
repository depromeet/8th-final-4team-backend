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
	@PostMapping("/api/v1/challenge")
	public ApiResponse<ChallengeSimpleInfoResponse> createNewChallenge(@Valid @RequestBody ChallengeCreateRequest request, @LoginMember MemberSession memberSession) {
		return ApiResponse.of(challengeService.createNewChallenge(request, memberSession.getMemberId()));
	}

	@ApiOperation("특정 챌린지의 상세정보를 불러오는 API")
	@GetMapping("/api/v1/challenge")
	public ApiResponse<ChallengeInfoResponse> getChallengeInfo(@Valid ChallengeRetrieveRequest request, @LoginMember MemberSession memberSession) {
		return ApiResponse.of(challengeService.getDetailChallengeInfo(request, memberSession.getMemberId()));
	}

	@ApiOperation("내가 포함하고 있는 챌린지 리스트를 불러오는 API")
	@GetMapping("/api/v1/challenge/my")
	public ApiResponse<List<ChallengeSimpleInfoResponse>> getMyChallengesInfo(@LoginMember MemberSession memberSession) {
		return ApiResponse.of(challengeService.getMyChallengeInfo(memberSession.getMemberId()));
	}

	@ApiOperation("챌린지의 새로운 초대키를 발급하는 API")
	@PutMapping("/api/v1/challenge/inviteKey")
	public ApiResponse<String> creatChallengeInviteKey(@Valid @RequestBody ChallengeCreateInvitationKeyRequest request, @LoginMember MemberSession memberSession) {
		return ApiResponse.of(challengeService.createInvitationKey(request, memberSession.getMemberId()));
	}

	@ApiOperation("발급된 초대키를 통해 챌린지에 참가하는 API")
	@PutMapping("/api/v1/challenge/invite")
	public ApiResponse<ChallengeSimpleInfoResponse> inviteMemberByInvitationKey(@Valid @RequestBody ChallengeInviteRequest request, @LoginMember MemberSession memberSession) {
		return ApiResponse.of(challengeService.inviteNewMemberWithInvitationKey(request, memberSession.getMemberId()));
	}

	@ApiOperation("초대키를 통해 챌린지의 간단한 정보를 가져오는 API")
	@GetMapping("/api/v1/challenge/invite")
	public ApiResponse<ChallengeSimpleInfoResponse> getInviteChallengeInfo(@Valid @RequestParam String invitationKey) {
		return ApiResponse.of(challengeService.getSimpleChallengeInfoByInvitationKey(invitationKey));
	}

}
