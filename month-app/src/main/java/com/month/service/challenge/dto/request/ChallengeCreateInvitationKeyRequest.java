package com.month.service.challenge.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChallengeCreateInvitationKeyRequest {

	private String challengeUuid;

	private ChallengeCreateInvitationKeyRequest(String challengeUuid) {
		this.challengeUuid = challengeUuid;
	}

	public static ChallengeCreateInvitationKeyRequest testInstance(String challengeUuid) {
		return new ChallengeCreateInvitationKeyRequest(challengeUuid);
	}

}
