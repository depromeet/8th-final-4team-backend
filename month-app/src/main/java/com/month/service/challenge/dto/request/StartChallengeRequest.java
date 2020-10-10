package com.month.service.challenge.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class StartChallengeRequest {

	@NotNull
	private Long challengePlanId;

	private StartChallengeRequest(Long challengePlanId) {
		this.challengePlanId = challengePlanId;
	}

	public static StartChallengeRequest testInstance(Long challengePlanId) {
		return new StartChallengeRequest(challengePlanId);
	}

}
