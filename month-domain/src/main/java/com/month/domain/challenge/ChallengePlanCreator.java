package com.month.domain.challenge;

public final class ChallengePlanCreator {

	public static ChallengePlan create(String name, String description, int period, int maxMembersCount) {
		return ChallengePlan.builder()
				.name(name)
				.description(description)
				.period(period)
				.maxMembersCount(maxMembersCount)
				.build();
	}

}
