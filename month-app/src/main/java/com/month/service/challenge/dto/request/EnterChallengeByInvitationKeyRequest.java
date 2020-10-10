package com.month.service.challenge.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EnterChallengeByInvitationKeyRequest {

	private String invitationKey;

	private EnterChallengeByInvitationKeyRequest(String invitationKey) {
		this.invitationKey = invitationKey;
	}

	public static EnterChallengeByInvitationKeyRequest testInstance(String invitationKey) {
		return new EnterChallengeByInvitationKeyRequest(invitationKey);
	}

}
