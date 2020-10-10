package com.month.service.challenge.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RefreshChallengeInvitationKeyRequest {

	private Long challengePlanId;

	private RefreshChallengeInvitationKeyRequest(Long challengePlanId) {
		this.challengePlanId = challengePlanId;
	}

	public static RefreshChallengeInvitationKeyRequest testInstance(Long challengePlanId) {
		return new RefreshChallengeInvitationKeyRequest(challengePlanId);
	}

}
