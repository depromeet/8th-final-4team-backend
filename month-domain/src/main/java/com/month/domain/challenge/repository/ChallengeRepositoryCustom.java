package com.month.domain.challenge.repository;

import com.month.domain.challenge.Challenge;

import java.util.List;

public interface ChallengeRepositoryCustom {

	Challenge findChallengeByUuid(String uuid);

	List<Challenge> findChallengesByMemberId(Long memberId);

	Challenge findChallengeByInvitationKey(String invitationKey);

}
