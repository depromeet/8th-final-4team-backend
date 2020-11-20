package com.month.service.challenge.dto.response;

import com.month.domain.challenge.Challenge;
import com.month.domain.challenge.ChallengeType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ChallengeResponse {

	private final String uuid;

	private final String name;

	private final ChallengeType type;

	private final String color;

	private final LocalDateTime startDateTime;

	private final LocalDateTime endDateTime;

	private final int membersCount;

	public static ChallengeResponse of(Challenge challenge) {
		return new ChallengeResponse(challenge.getUuid(), challenge.getName(), challenge.getType(), challenge.getColor(),
				challenge.getStartDateTime(), challenge.getEndDateTime(), challenge.getMembersCount());
	}

}
