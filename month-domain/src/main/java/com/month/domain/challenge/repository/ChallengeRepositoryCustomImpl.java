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
	public List<Challenge> findChallengesByMemberId(Long memberId) {
		return queryFactory.selectFrom(challenge).distinct()
				.innerJoin(challenge.challengeMemberMappers, challengeMemberMapper).fetchJoin()
				.where(
						challengeMemberMapper.memberId.eq(memberId)
				).fetch();
	}

	@Override
	public List<Challenge> findStartedChallengesByMemberId(Long memberId) {
		return queryFactory.selectFrom(challenge).distinct()
				.innerJoin(challenge.challengeMemberMappers, challengeMemberMapper).fetchJoin()
				.where(
						challengeMemberMapper.memberId.eq(memberId),
						challenge.dateTimeInterval.startDateTime.before(LocalDateTime.now())
				).fetch();
	}


	/**
	 * Challenge A  -  ChallengeMemberMapper1 (나) -> 페치 조인시 애만 조회 되는 상황
	 * Challenge A  -  ChallengeMemberMapper2 (친구) -> 애가 조회 안되는 상황
	 * <p>
	 * 지금처럼 비즈니스 로직 단에서 필터링 하는 방법보다
	 * 쿼리 조회	시, membrerId (1, 2, 3)리스트가 주어졌을때,
	 * 1, 2, 3 모두 ChallengeMemberMapper가 있는 Challenge 리스트를 조회하는 방법?
	 */
	// TODO: 방법이 당장 생각 안나서 임시로 페치조인 푼 메소드 하나 추가해서 사용하고 있는데 개선이 필요합니다.
	@Override
	public List<Challenge> findNoFetchChallengesByMemberId(Long memberId) {
		return queryFactory.selectFrom(challenge).distinct()
				.innerJoin(challenge.challengeMemberMappers, challengeMemberMapper)
				.where(
						challengeMemberMapper.memberId.eq(memberId)
				).fetch();
	}

}
