package com.month.service.challenge.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class RefreshChallengeInvitationKeyRequest {

	@NotNull
	private Long challengePlanId;

	private RefreshChallengeInvitationKeyRequest(Long challengePlanId) {
		this.challengePlanId = challengePlanId;
	}

	public static RefreshChallengeInvitationKeyRequest testInstance(Long challengePlanId) {
		return new RefreshChallengeInvitationKeyRequest(challengePlanId);
	}

}
