package com.month.domain.challenge.repository;

import com.month.domain.challenge.ChallengePlan;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static com.month.domain.challenge.QChallengePlan.challengePlan;

@RequiredArgsConstructor
public class ChallengePlanRepositoryCustomImpl implements ChallengePlanRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public ChallengePlan findActiveChallengePlanById(Long challengePlanId) {
		return queryFactory.selectFrom(challengePlan)
				.where(
						challengePlan.id.eq(challengePlanId),
						challengePlan.isActive.eq(true)
				).fetchOne();
	}

	@Override
	public ChallengePlan findActiveChallengePlanByInvitationKey(String invitationKey) {
		return queryFactory.selectFrom(challengePlan)
				.where(
						challengePlan.invitationKey.invitationKey.eq(invitationKey),
						challengePlan.isActive.eq(true)
				).fetchOne();
	}

}
