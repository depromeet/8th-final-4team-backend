package com.month.service.challenge.dto.response;

import com.month.domain.challenge.CertifyType;
import com.month.domain.challenge.Challenge;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ChallengeSimpleInfoResponse {

	private final String challengeUuid;

	private final String name;

	private final String description;

	private final LocalDateTime startDateTime;

	private final LocalDateTime endDateTime;

	private final int membersCount;

	private final CertifyType certifyType;

	public static ChallengeSimpleInfoResponse of(Challenge challenge) {
		return new ChallengeSimpleInfoResponse(challenge.getUuid(), challenge.getName(), challenge.getDescription(),
				challenge.getStartDateTime(), challenge.getEndDateTime(), challenge.getMembersCount(), challenge.getCertifyType());
	}

}
