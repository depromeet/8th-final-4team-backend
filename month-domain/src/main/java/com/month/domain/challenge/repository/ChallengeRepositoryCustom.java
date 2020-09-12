package com.month.domain.challenge.repository;

import com.month.domain.challenge.Challenge;

public interface ChallengeRepositoryCustom {

	Challenge findChallengeByUuid(String uuid);
	
}
