package com.month.service.challenge.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RetrieveChallengePlanInvitationKeyRequest {

	private Long challengePlanId;

	public RetrieveChallengePlanInvitationKeyRequest(Long challengePlanId) {
		this.challengePlanId = challengePlanId;
	}

}
