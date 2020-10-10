package com.month.service.challenge.dto.response;

import com.month.domain.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberInChallengeResponse {

	private final Long id;

	private final String email;

	private final String name;

	private final String profileUrl;

	public static MemberInChallengeResponse of(Member member) {
		return new MemberInChallengeResponse(member.getId(), member.getEmail(), member.getName(), member.getPhotoUrl());
	}

}
