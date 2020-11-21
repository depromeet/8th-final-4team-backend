package com.month.domain.challenge;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ChallengeCreator {

	public static Challenge create(String name, ChallengeType type, String color, LocalDateTime startDateTime, LocalDateTime endDateTime) {
		return Challenge.builder()
				.name(name)
				.type(type)
				.color(color)
				.startDateTime(startDateTime)
				.endDateTime(endDateTime)
				.build();
	}

	public static Challenge create(String name, LocalDateTime startDateTime, LocalDateTime endDateTime) {
		return Challenge.builder()
				.name(name)
				.type(ChallengeType.FINANCIAL)
				.color("#000000")
				.startDateTime(startDateTime)
				.endDateTime(endDateTime)
				.build();
	}

}
