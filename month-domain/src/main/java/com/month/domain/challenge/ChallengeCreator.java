package com.month.domain.challenge;

import java.time.LocalDateTime;

public final class ChallengeCreator {

	public static Challenge create(String name) {
		return Challenge.builder()
				.name(name)
				.startDateTime(LocalDateTime.of(2020, 9, 1, 0, 0))
				.endDateTime(LocalDateTime.of(2030, 9, 1, 0, 0))
				.certifyType(CertifyType.PICTURE)
				.build();
	}

	public static Challenge create(String name, String description, LocalDateTime startDateTime, LocalDateTime endDateTime, CertifyType certifyType) {
		return Challenge.builder()
				.name(name)
				.description(description)
				.startDateTime(startDateTime)
				.endDateTime(endDateTime)
				.certifyType(certifyType)
				.build();
	}

}
