package com.month.domain.challenge.repository;

import com.month.domain.challenge.ChallengePlan;

public interface ChallengePlanRepositoryCustom {

	ChallengePlan findActiveChallengePlanById(Long challengePlanId);

	ChallengePlan findActiveChallengePlanByInvitationKey(String invitationKey);

}
