package com.month.domain.challenge.repository;

import com.month.domain.challenge.ChallengePlan;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.month.domain.challenge.QChallengePlan.challengePlan;
import static com.month.domain.challenge.QChallengePlanMemberMapper.challengePlanMemberMapper;

@RequiredArgsConstructor
public class ChallengePlanRepositoryCustomImpl implements ChallengePlanRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public ChallengePlan findActiveChallengePlanById(Long challengePlanId) {
		return queryFactory.selectFrom(challengePlan).distinct()
				.innerJoin(challengePlan.challengePlanMemberMapperList, challengePlanMemberMapper).fetchJoin()
				.where(
						challengePlan.id.eq(challengePlanId),
						challengePlan.isActive.isTrue()
				).fetchOne();
	}

	@Override
	public ChallengePlan findActiveChallengePlanByInvitationKey(String invitationKey) {
		return queryFactory.selectFrom(challengePlan).distinct()
				.innerJoin(challengePlan.challengePlanMemberMapperList, challengePlanMemberMapper).fetchJoin()
				.where(
						challengePlan.invitationKey.invitationKey.eq(invitationKey),
						challengePlan.isActive.isTrue()
				).fetchOne();
	}

	@Override
	public List<ChallengePlan> findActiveChallengePlanByMemberId(Long memberId) {
		return queryFactory.selectFrom(challengePlan).distinct()
				.innerJoin(challengePlan.challengePlanMemberMapperList, challengePlanMemberMapper).fetchJoin()
				.where(
						challengePlanMemberMapper.memberId.eq(memberId),
						challengePlan.isActive.isTrue()
				).fetch();
	}

}
