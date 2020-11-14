package com.month.domainservice.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberAchieveRateResponse {

	private final int totalChallengesCount;

	private final double achieveChallengeRate;

	public static MemberAchieveRateResponse of(int totalChallengesCount, double achieveChallengeRate) {
		return new MemberAchieveRateResponse(totalChallengesCount, achieveChallengeRate);
	}

}
