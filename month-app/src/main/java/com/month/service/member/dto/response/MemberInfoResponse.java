package com.month.service.member.dto.response;

import com.month.domain.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberInfoResponse {

	private final String email;

	private final String name;

	private final String photoUrl;

	private final String providerId;

	public static MemberInfoResponse of(Member member) {
		return new MemberInfoResponse(member.getEmail(), member.getName(), member.getPhotoUrl(), member.getProviderId());
	}

}
