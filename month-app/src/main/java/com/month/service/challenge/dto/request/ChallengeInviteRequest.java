package com.month.service.challenge.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChallengeInviteRequest {

	private String invitationKey;

	private ChallengeInviteRequest(String invitationKey) {
		this.invitationKey = invitationKey;
	}

	public static ChallengeInviteRequest testInstance(String invitationKey) {
		return new ChallengeInviteRequest(invitationKey);
	}

}
