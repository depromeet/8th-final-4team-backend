package com.month.service.challenge.dto.response;

import com.month.domain.challenge.Challenge;
import com.month.domain.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ChallengeInfoResponse {

	private final String uuid;

	private final String name;

	private final String description;

	private final int membersCount;

	private final LocalDateTime startDateTime;

	private final LocalDateTime endDateTime;

	private List<MemberInChallengeResponse> members = new ArrayList<>();

	public static ChallengeInfoResponse of(Challenge challenge, List<Member> members) {
		ChallengeInfoResponse response = new ChallengeInfoResponse(challenge.getUuid(), challenge.getName(), challenge.getDescription(),
				challenge.getMembersCount(), challenge.getStartDateTime(), challenge.getEndDateTime());
		response.members.addAll(members.stream()
				.map(MemberInChallengeResponse::of)
				.collect(Collectors.toList()));
		return response;
	}

}
