package com.month.service.challenge.dto.response;

import com.month.domain.challenge.ChallengePlan;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ChallengePlanInvitationInfo {

	private final String name;

	private final String description;

	private final int maxMembersCount;

	private final int currentMembersCount;

	private final int period;

	public static ChallengePlanInvitationInfo of(ChallengePlan challengePlan) {
		return new ChallengePlanInvitationInfo(challengePlan.getName(), challengePlan.getDescription(),
				challengePlan.getMaxMembersCount(), challengePlan.getCurrentMembersCount(), challengePlan.getPeriod());
	}

}
