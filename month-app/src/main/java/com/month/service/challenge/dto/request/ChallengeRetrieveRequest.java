package com.month.service.challenge.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChallengeRetrieveRequest {

	private String uuid;

	private ChallengeRetrieveRequest(String uuid) {
		this.uuid = uuid;
	}

	public static ChallengeRetrieveRequest testInstance(String uuid) {
		return new ChallengeRetrieveRequest(uuid);
	}

}
