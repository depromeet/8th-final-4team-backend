package com.month.domain.challenge.repository;

import com.month.domain.challenge.Challenge;

import java.util.List;

public interface ChallengeRepositoryCustom {

	List<Challenge> findActiveChallengesByMemberId(Long memberId);

	List<Challenge> findChallengesByMemberId(Long memberId);

}
