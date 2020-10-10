package com.month.domain.challenge;

import java.time.LocalDateTime;

public final class ChallengeCreator {

	public static Challenge create(String name, LocalDateTime startDateTime, LocalDateTime endDateTime) {
		return Challenge.builder()
				.name(name)
				.startDateTime(startDateTime)
				.endDateTime(endDateTime)
				.build();
	}

}
