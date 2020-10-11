package com.month.service.challenge;

import com.month.domain.challenge.Challenge;
import com.month.domain.challenge.ChallengePlan;
import com.month.domain.challenge.ChallengePlanRepository;
import com.month.domain.challenge.ChallengeRepository;
import com.month.domain.member.MemberRepository;
import com.month.service.challenge.dto.request.StartChallengeRequest;
import com.month.service.challenge.dto.response.ChallengeInfoResponse;
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
	public ChallengeInfoResponse startChallenge(StartChallengeRequest request, Long memberId) {
		ChallengePlan challengePlan = ChallengeServiceUtils.findActiveChallengePlanById(challengePlanRepository, request.getChallengePlanId());
		challengePlan.validateCreator(memberId);
		challengePlan.inactiveChallengePlan();
		Challenge newChallenge = challengeRepository.save(challengePlan.convertToChallenge());
		return ChallengeInfoResponse.of(newChallenge, memberRepository.findAllById(newChallenge.getMemberIds()));
	}

	@Transactional(readOnly = true)
	public List<ChallengeInfoResponse> retrieveMyChallengeList(Long memberId) {
		List<Challenge> challenges = challengeRepository.findActiveChallengesByMemberId(memberId);
		return challenges.stream()
				.map(challenge -> ChallengeInfoResponse.of(challenge, memberRepository.findAllById(challenge.getMemberIds())))
				.collect(Collectors.toList());
	}

}
