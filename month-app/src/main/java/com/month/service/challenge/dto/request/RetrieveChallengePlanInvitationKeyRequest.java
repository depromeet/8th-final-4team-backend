package com.month.service.challenge.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class RetrieveChallengePlanInvitationKeyRequest {

	@NotNull
	private Long challengePlanId;

	public RetrieveChallengePlanInvitationKeyRequest(Long challengePlanId) {
		this.challengePlanId = challengePlanId;
	}

}
