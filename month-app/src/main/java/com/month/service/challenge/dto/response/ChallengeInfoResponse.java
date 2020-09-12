package com.month.service.challenge.dto.response;

import com.month.domain.challenge.CertifyType;
import com.month.domain.challenge.Challenge;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ChallengeInfoResponse {

	private final String challengeUuid;

	private final String name;

	private final String description;

	private final int membersCount;

	private final LocalDateTime startDateTime;

	private final LocalDateTime endDateTime;

	private final CertifyType certifyType;

	public static ChallengeInfoResponse of(Challenge challenge) {
		return new ChallengeInfoResponse(challenge.getUuid(), challenge.getName(), challenge.getDescription(),
				challenge.getMembersCount(), challenge.getStartDateTime(), challenge.getEndDateTime(), challenge.getCertifyType());
	}

}
