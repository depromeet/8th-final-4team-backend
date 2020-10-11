package com.month.event.challenge;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class AllMembersEnteredEvent {

	private final Long challengePlanId;

	public static AllMembersEnteredEvent of(Long challengePlanId) {
		return new AllMembersEnteredEvent(challengePlanId);
	}

}
