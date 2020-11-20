package com.month.domain.challenge.repository;

import com.month.domain.challenge.Challenge;
import com.month.domain.challenge.ChallengeMemberStatus;
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
						challenge.dateTimeInterval.startDateTime.before(LocalDateTime.now()),
						challengeMemberMapper.status.eq(ChallengeMemberStatus.APPROVED)
				).fetch();
	}

	// TODO: 임시로 페치조인 푼 메소드 하나 추가해서 사용하고 있는데 개선이 필요합니다.
	@Override
	public List<Challenge> findNoFetchStartedChallengesByMemberId(Long memberId) {
		return queryFactory.selectFrom(challenge).distinct()
				.innerJoin(challenge.challengeMemberMappers, challengeMemberMapper)
				.where(
						challengeMemberMapper.memberId.eq(memberId),
						challengeMemberMapper.status.eq(ChallengeMemberStatus.APPROVED)
				).fetch();
	}

	@Override
	public Challenge findChallengeByUuid(String uuid) {
		return queryFactory.selectFrom(challenge).distinct()
				.innerJoin(challenge.challengeMemberMappers, challengeMemberMapper).fetchJoin()
				.where(
						challenge.uuid.uuid.eq(uuid)
				).fetchOne();
	}

	@Override
	public Challenge findChallengeByInvitationKey(String invitationKey) {
		return queryFactory.selectFrom(challenge)
				.where(
						challenge.invitationKey.invitationKey.eq(invitationKey)
				).fetchOne();
	}

	@Override
	public List<Challenge> findPendingChallengeByMemberId(Long memberId) {
		return queryFactory.selectFrom(challenge).distinct()
				.innerJoin(challenge.challengeMemberMappers, challengeMemberMapper).fetchJoin()
				.where(
						challengeMemberMapper.memberId.eq(memberId),
						challengeMemberMapper.status.eq(ChallengeMemberStatus.PENDING)
				).fetch();
	}

}
