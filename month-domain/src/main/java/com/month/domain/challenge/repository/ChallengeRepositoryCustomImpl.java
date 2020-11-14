package com.month.domain.challenge.repository;

import com.month.domain.challenge.Challenge;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import static com.month.domain.challenge.QChallenge.challenge;
import static com.month.domain.challenge.QChallengeMemberMapper.challengeMemberMapper;

@RequiredArgsConstructor
public class ChallengeRepositoryCustomImpl implements ChallengeRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<Challenge> findActiveChallengesByMemberId(Long memberId) {
		return queryFactory.selectFrom(challenge).distinct()
				.innerJoin(challenge.challengeMemberMappers, challengeMemberMapper).fetchJoin()
				.where(
						challengeMemberMapper.memberId.eq(memberId),
						challenge.dateTimeInterval.endDateTime.after(LocalDateTime.now()),
						challenge.dateTimeInterval.startDateTime.before(LocalDateTime.now())
				).fetch();
	}

	@Override
	public List<Challenge> findChallengesByMemberId(Long memberId) {
		return queryFactory.selectFrom(challenge).distinct()
				.innerJoin(challenge.challengeMemberMappers, challengeMemberMapper).fetchJoin()
				.where(
						challengeMemberMapper.memberId.eq(memberId)
				).fetch();
	}

}
