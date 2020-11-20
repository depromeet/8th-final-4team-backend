package com.month.service.challenge.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class GetInvitationKeyRequest {

	@NotBlank
	private String challengeUuid;

	private GetInvitationKeyRequest(@NotBlank String challengeUuid) {
		this.challengeUuid = challengeUuid;
	}

	public static GetInvitationKeyRequest testInstance(String challengeUuid) {
		return new GetInvitationKeyRequest(challengeUuid);
	}

}
