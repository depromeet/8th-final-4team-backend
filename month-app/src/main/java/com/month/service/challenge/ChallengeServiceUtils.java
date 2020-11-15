package com.month.service.challenge;

import com.month.domain.challenge.ChallengePlan;
import com.month.domain.challenge.ChallengePlanRepository;
import com.month.exception.NotFoundException;

import static com.month.exception.type.ExceptionDescriptionType.CHALLENGE;
import static com.month.exception.type.ExceptionDescriptionType.INVITATION_KEY;

final class ChallengeServiceUtils {

	static ChallengePlan findActiveChallengePlanById(ChallengePlanRepository challengePlanRepository, Long id) {
		ChallengePlan challengePlan = challengePlanRepository.findActiveChallengePlanById(id);
		if (challengePlan == null) {
			throw new NotFoundException(String.format("해당 하는 (%s) 챌린지는 존재하지 않습니다", id), CHALLENGE);
		}
		return challengePlan;
	}

	static ChallengePlan findActiveChallengePlanByInvitationKey(ChallengePlanRepository challengePlanRepository, String invitationKey) {
		ChallengePlan challengePlan = challengePlanRepository.findActiveChallengePlanByInvitationKey(invitationKey);
		if (challengePlan == null) {
			throw new NotFoundException(String.format("해당하는 초대장 (%s) 을 가진 챌린지는 없습니다 ", invitationKey), INVITATION_KEY);
		}
		return challengePlan;
	}

}
