package com.month.service.challenge;

import com.month.domain.challenge.Challenge;
import com.month.domain.challenge.ChallengeRepository;
import com.month.domain.member.Member;
import com.month.domain.member.MemberRepository;
import com.month.service.challenge.dto.request.ChallengeCreateInvitationKeyRequest;
import com.month.service.challenge.dto.request.ChallengeCreateRequest;
import com.month.service.challenge.dto.request.ChallengeInviteRequest;
import com.month.service.challenge.dto.request.ChallengeRetrieveRequest;
import com.month.service.challenge.dto.response.ChallengeInfoResponse;
import com.month.service.challenge.dto.response.ChallengeSimpleInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ChallengeService {

	private final ChallengeRepository challengeRepository;
	private final MemberRepository memberRepository;

	@Transactional
	public ChallengeSimpleInfoResponse createNewChallenge(ChallengeCreateRequest request, Long memberId) {
		Challenge challenge = request.toEntity();
		challenge.addCreator(memberId);
		return ChallengeSimpleInfoResponse.of(challengeRepository.save(challenge));
	}

	@Transactional(readOnly = true)
	public ChallengeInfoResponse getDetailChallengeInfo(ChallengeRetrieveRequest request, Long memberId) {
		Challenge challenge = ChallengeServiceUtils.findChallengeByUuid(challengeRepository, request.getUuid());
		challenge.validateMemberInChallenge(memberId);
		List<Member> membersInChallenge = memberRepository.findMembersByMemberIds(challenge.getMembersInChallenge());
		return ChallengeInfoResponse.of(challenge, membersInChallenge);
	}

	@Transactional(readOnly = true)
	public List<ChallengeSimpleInfoResponse> getMyChallengeInfo(Long memberId) {
		List<Challenge> challenges = challengeRepository.findChallengesByMemberId(memberId);
		return challenges.stream()
				.map(ChallengeSimpleInfoResponse::of)
				.collect(Collectors.toList());
	}

	@Transactional
	public String createInvitationKey(ChallengeCreateInvitationKeyRequest request, Long memberId) {
		Challenge challenge = ChallengeServiceUtils.findChallengeByUuid(challengeRepository, request.getChallengeUuid());
		challenge.createNewInvitationKey(memberId);
		return challenge.getInvitationKey();
	}

	@Transactional
	public ChallengeSimpleInfoResponse inviteNewMemberWithInvitationKey(ChallengeInviteRequest request, Long memberId) {
		Challenge challenge = ChallengeServiceUtils.findChallengeByInvitationKey(challengeRepository, request.getInvitationKey());
		challenge.validateNotExpiredInvitationKey();
		challenge.addParticipator(memberId);
		return ChallengeSimpleInfoResponse.of(challenge);
	}

	@Transactional(readOnly = true)
	public ChallengeSimpleInfoResponse getSimpleChallengeInfoByInvitationKey(String invitationKey) {
		Challenge challenge = ChallengeServiceUtils.findChallengeByInvitationKey(challengeRepository, invitationKey);
		challenge.validateNotExpiredInvitationKey();
		return ChallengeSimpleInfoResponse.of(challenge);
	}

}
