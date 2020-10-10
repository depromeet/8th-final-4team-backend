package com.month.service.challenge.dto.response;

import com.month.domain.challenge.ChallengePlan;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ChallengePlanInfoResponse {

	private final Long id;

	private final String name;

	private final String description;

	private final int period;

	private final int maxMembersCount;

	private final int currentMembersCount;

	public static ChallengePlanInfoResponse of(ChallengePlan challengePlan) {
		return new ChallengePlanInfoResponse(challengePlan.getId(), challengePlan.getName(), challengePlan.getDescription(),
				challengePlan.getPeriod(), challengePlan.getMaxMembersCount(), challengePlan.getCurrentMembersCount());
	}

}
