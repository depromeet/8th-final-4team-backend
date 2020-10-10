package com.month.service.challenge.dto.response;

import com.month.domain.challenge.ChallengePlan;
import com.month.domain.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ToString
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ChallengePlanInfoResponse {

	private final Long id;

	private final String name;

	private final String description;

	private final int period;

	private final int maxMembersCount;

	private final int currentMembersCount;

	private final List<MemberInChallengeResponse> members = new ArrayList<>();

	public static ChallengePlanInfoResponse of(ChallengePlan challengePlan, List<Member> members) {
		ChallengePlanInfoResponse response = new ChallengePlanInfoResponse(challengePlan.getId(), challengePlan.getName(), challengePlan.getDescription(),
				challengePlan.getPeriod(), challengePlan.getMaxMembersCount(), challengePlan.getCurrentMembersCount());
		response.members.addAll(members.stream()
				.map(MemberInChallengeResponse::of)
				.collect(Collectors.toList()));
		return response;
	}

}
