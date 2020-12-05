package com.month.service.challenge.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class RejectInviteChallengeRequest {

	@NotBlank
	private String invitationKey;

	private RejectInviteChallengeRequest(String invitationKey) {
		this.invitationKey = invitationKey;
	}

	public static RejectInviteChallengeRequest testInstance(String issueInvitationKey) {
		return new RejectInviteChallengeRequest(issueInvitationKey);
	}

}
