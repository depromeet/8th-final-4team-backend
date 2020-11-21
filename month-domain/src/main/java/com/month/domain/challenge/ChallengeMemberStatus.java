package com.month.domain.challenge;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ChallengeMemberStatus {

	APPROVED(true),
	PENDING(false),
	REJECT(false);

	private final boolean isParticipating;

}
