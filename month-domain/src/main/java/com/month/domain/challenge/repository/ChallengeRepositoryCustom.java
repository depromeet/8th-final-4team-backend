package com.month.domain.challenge.repository;

import com.month.domain.challenge.Challenge;

import java.util.List;

public interface ChallengeRepositoryCustom {

	List<Challenge> findChallengesByMemberId(Long memberId);

	List<Challenge> findStartedChallengesByMemberId(Long memberId);

	List<Challenge> findNoFetchStartedChallengesByMemberId(Long memberId);

	Challenge findChallengeByUuid(String uuid);

	Challenge findChallengeByInvitationKey(String invitationKey);

	List<Challenge> findPendingChallengeByMemberId(Long memberId);

}
