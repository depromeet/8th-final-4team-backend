package com.month.service.challenge.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class EnterChallengeByInvitationKeyRequest {

	@NotBlank
	private String invitationKey;

	private EnterChallengeByInvitationKeyRequest(String invitationKey) {
		this.invitationKey = invitationKey;
	}

	public static EnterChallengeByInvitationKeyRequest testInstance(String invitationKey) {
		return new EnterChallengeByInvitationKeyRequest(invitationKey);
	}

}
