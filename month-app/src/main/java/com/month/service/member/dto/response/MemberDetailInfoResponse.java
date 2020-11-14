package com.month.service.member.dto.response;

import com.month.domain.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberDetailInfoResponse {

	private final Long id;

	private final String email;

	private final String name;

	private final String photoUrl;

	private final int totalChallengesCount;

	private final double achieveChallengeRate;

	public static MemberDetailInfoResponse of(Member member, int totalChallengesCount, double achieveRate) {
		return new MemberDetailInfoResponse(member.getId(), member.getEmail(), member.getName(), member.getPhotoUrl(),
				totalChallengesCount, achieveRate);
	}

}