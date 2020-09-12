package com.month.domain.challenge.repository;

import com.month.domain.challenge.Challenge;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static com.month.domain.challenge.QChallenge.challenge;

@RequiredArgsConstructor
public class ChallengeRepositoryCustomImpl implements ChallengeRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public Challenge findChallengeByUuid(String uuid) {
		return queryFactory.selectFrom(challenge)
				.where(
						challenge.uuid.uuid.eq(uuid)
				).fetchOne();
	}

}
