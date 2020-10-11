package com.month.domain.challenge.repository;

import com.month.domain.challenge.ChallengePlan;

import java.util.List;

public interface ChallengePlanRepositoryCustom {

	ChallengePlan findActiveChallengePlanById(Long challengePlanId);

	ChallengePlan findActiveChallengePlanByInvitationKey(String invitationKey);

	List<ChallengePlan> findActiveChallengePlanByMemberId(Long memberId);

}
