package com.month.service.challenge.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class ParticipateChallengeRequest {

	@NotBlank
	private String invitationKey;

	private ParticipateChallengeRequest(String invitationKey) {
		this.invitationKey = invitationKey;
	}

	public static ParticipateChallengeRequest testInstance(String issueInvitationKey) {
		return new ParticipateChallengeRequest(issueInvitationKey);
	}

}
