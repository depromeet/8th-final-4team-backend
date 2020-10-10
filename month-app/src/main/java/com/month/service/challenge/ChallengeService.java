package com.month.service.challenge;

import com.month.domain.challenge.ChallengePlan;
import com.month.domain.challenge.ChallengePlanRepository;
import com.month.domain.challenge.ChallengeRepository;
import com.month.service.challenge.dto.request.CreateChallengePlanRequest;
import com.month.service.challenge.dto.request.StartChallengeRequest;
import com.month.service.challenge.dto.response.ChallengePlanInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ChallengeService {

	private final ChallengePlanRepository challengePlanRepository;
	private final ChallengeRepository challengeRepository;

	@Transactional
	public ChallengePlanInfoResponse createChallengePlan(CreateChallengePlanRequest request, Long memberId) {
		ChallengePlan challengePlan = request.toEntity();
		challengePlan.addCreator(memberId);
		return ChallengePlanInfoResponse.of(challengePlanRepository.save(challengePlan));
	}
	
	@Transactional
	public void startChallenge(StartChallengeRequest request, Long memberId) {
		ChallengePlan challengePlan = ChallengeServiceUtils.findChallengePlanById(challengePlanRepository, request.getChallengePlanId());
		challengePlan.validateCreator(memberId);
		challengePlan.inactiveChallengePlan();
		challengeRepository.save(challengePlan.convertToChallenge());
	}

}
