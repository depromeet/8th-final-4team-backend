package com.month.service.challenge;

import com.month.domain.challenge.ChallengePlan;
import com.month.domain.challenge.ChallengePlanRepository;

public final class ChallengeServiceUtils {

	static ChallengePlan findChallengePlanById(ChallengePlanRepository challengePlanRepository, Long id) {
		ChallengePlan challengePlan = challengePlanRepository.findChallengePlanById(id);
		if (challengePlan == null) {
			throw new IllegalArgumentException(String.format("해당 하는 (%s) 챌린지는 존재하지 않습니다", id));
		}
		return challengePlan;
	}

}
