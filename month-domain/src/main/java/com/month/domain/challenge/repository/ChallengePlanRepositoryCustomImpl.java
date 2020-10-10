package com.month.domain.challenge.repository;

import com.month.domain.challenge.ChallengePlan;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static com.month.domain.challenge.QChallengePlan.challengePlan;
import static com.month.domain.challenge.QChallengePlanMemberMapper.challengePlanMemberMapper;

@RequiredArgsConstructor
public class ChallengePlanRepositoryCustomImpl implements ChallengePlanRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public ChallengePlan findChallengePlanById(Long challengePlanId) {
		return queryFactory.selectFrom(challengePlan)
				.where(
						challengePlan.id.eq(challengePlanId)
				).fetchOne();
	}

}
