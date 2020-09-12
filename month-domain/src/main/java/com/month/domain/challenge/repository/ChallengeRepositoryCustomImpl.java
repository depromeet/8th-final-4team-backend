package com.month.domain.challenge.repository;

import com.month.domain.challenge.Challenge;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.month.domain.challenge.QChallenge.challenge;
import static com.month.domain.challenge.QChallengeMemberMapper.challengeMemberMapper;

@RequiredArgsConstructor
public class ChallengeRepositoryCustomImpl implements ChallengeRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public Challenge findChallengeByUuid(String uuid) {
		return queryFactory.selectFrom(challenge).distinct()
				.innerJoin(challenge.memberMappers, challengeMemberMapper).fetchJoin()
				.where(
						challenge.uuid.uuid.eq(uuid)
				).fetchOne();
	}

	@Override
	public List<Challenge> findChallengesByMemberId(Long memberId) {
		return queryFactory.selectFrom(challenge).distinct()
				.innerJoin(challenge.memberMappers, challengeMemberMapper).fetchJoin()
				.where(
						challengeMemberMapper.memberId.eq(memberId)
				).fetch();
	}

	@Override
	public Challenge findChallengeByInvitationKey(String invitationKey) {
		return queryFactory.selectFrom(challenge)
				.where(
						challenge.invitationKey.invitationKey.eq(invitationKey)
				).fetchOne();
	}

}
