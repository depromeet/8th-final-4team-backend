package com.month.service.challenge.dto.response;

import com.month.domain.challenge.CertifyType;
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

	private final String challengeUuid;

	private final String name;

	private final String description;

	private final int membersCount;

	private final LocalDateTime startDateTime;

	private final LocalDateTime endDateTime;

	private final CertifyType certifyType;

	private final List<MemberInChallengeResponse> membersInChallenge = new ArrayList<>();

	public static ChallengeInfoResponse of(Challenge challenge, List<Member> members) {
		List<MemberInChallengeResponse> membersInfo = members.stream()
				.map(MemberInChallengeResponse::of)
				.collect(Collectors.toList());
		ChallengeInfoResponse response = new ChallengeInfoResponse(challenge.getUuid(), challenge.getName(), challenge.getDescription(),
				challenge.getMembersCount(), challenge.getStartDateTime(), challenge.getEndDateTime(), challenge.getCertifyType());
		response.membersInChallenge.addAll(membersInfo);
		return response;
	}

}
