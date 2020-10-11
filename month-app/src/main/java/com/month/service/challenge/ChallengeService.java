package com.month.service.challenge;

import com.month.domain.challenge.Challenge;
import com.month.domain.challenge.ChallengePlan;
import com.month.domain.challenge.ChallengePlanRepository;
import com.month.domain.challenge.ChallengeRepository;
import com.month.domain.member.MemberRepository;
import com.month.service.challenge.dto.request.CreateChallengePlanRequest;
import com.month.service.challenge.dto.request.EnterChallengeByInvitationKeyRequest;
import com.month.service.challenge.dto.request.RefreshChallengeInvitationKeyRequest;
import com.month.service.challenge.dto.request.RetrieveChallengePlanInvitationKeyRequest;
import com.month.service.challenge.dto.request.StartChallengeRequest;
import com.month.service.challenge.dto.response.ChallengeInfoResponse;
import com.month.service.challenge.dto.response.ChallengePlanInfoResponse;
import com.month.service.challenge.dto.response.ChallengePlanInvitationInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ChallengeService {

	private final ChallengePlanRepository challengePlanRepository;
	private final ChallengeRepository challengeRepository;
	private final MemberRepository memberRepository;

	@Transactional
	public ChallengePlanInfoResponse createChallengePlan(CreateChallengePlanRequest request, Long memberId) {
		ChallengePlan challengePlan = request.toEntity();
		challengePlan.addCreator(memberId);
		return ChallengePlanInfoResponse.of(
				challengePlanRepository.save(challengePlan),
				memberRepository.findAllById(challengePlan.getMemberIds()));
	}

	@Transactional(readOnly = true)
	public List<ChallengePlanInfoResponse> retrieveMyChallengePlans(Long memberId) {
		List<ChallengePlan> challengePlans = challengePlanRepository.findActiveChallengePlanByMemberId(memberId);
		return challengePlans.stream()
				.map(challengePlan -> ChallengePlanInfoResponse.of(challengePlan, memberRepository.findAllById(challengePlan.getMemberIds())))
				.collect(Collectors.toList());
	}

	@Transactional
	public ChallengeInfoResponse startChallenge(StartChallengeRequest request, Long memberId) {
		ChallengePlan challengePlan = ChallengeServiceUtils.findActiveChallengePlanById(challengePlanRepository, request.getChallengePlanId());
		challengePlan.validateCreator(memberId);
		challengePlan.inactiveChallengePlan();
		Challenge newChallenge = challengeRepository.save(challengePlan.convertToChallenge());
		return ChallengeInfoResponse.of(newChallenge, memberRepository.findAllById(newChallenge.getMemberIds()));
	}

	@Transactional(readOnly = true)
	public String getChallengePlanInvitationKey(RetrieveChallengePlanInvitationKeyRequest request, Long memberId) {
		ChallengePlan challengePlan = ChallengeServiceUtils.findActiveChallengePlanById(challengePlanRepository, request.getChallengePlanId());
		challengePlan.validateIsMember(memberId);
		return challengePlan.getInvitationKey();
	}

	@Transactional
	public String refreshChallengeInvitationKey(RefreshChallengeInvitationKeyRequest request, Long memberId) {
		ChallengePlan challengePlan = ChallengeServiceUtils.findActiveChallengePlanById(challengePlanRepository, request.getChallengePlanId());
		challengePlan.refreshInvitationKey(memberId);
		return challengePlan.getInvitationKey();
	}

	@Transactional(readOnly = true)
	public ChallengePlanInvitationInfo getChallengeInfoByInvitationKey(String invitationKey) {
		ChallengePlan challengePlan = ChallengeServiceUtils.findActiveChallengePlanByInvitationKey(challengePlanRepository, invitationKey);
		return ChallengePlanInvitationInfo.of(challengePlan);
	}

	@Transactional
	public void enterChallengeByInvitationKey(EnterChallengeByInvitationKeyRequest request, Long memberId) {
		ChallengePlan challengePlan = ChallengeServiceUtils.findActiveChallengePlanByInvitationKey(challengePlanRepository, request.getInvitationKey());
		challengePlan.addParticipator(memberId);
		// TODO 인원이 모두 들어오면 시작하도록 설정
	}

	@Transactional(readOnly = true)
	public List<ChallengeInfoResponse> retrieveMyChallengeList(Long memberId) {
		List<Challenge> challenges = challengeRepository.findActiveChallengesByMemberId(memberId);
		return challenges.stream()
				.map(challenge -> ChallengeInfoResponse.of(challenge, memberRepository.findAllById(challenge.getMemberIds())))
				.collect(Collectors.toList());
	}

}
