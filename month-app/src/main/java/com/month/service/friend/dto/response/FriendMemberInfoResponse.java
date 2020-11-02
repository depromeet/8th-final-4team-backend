package com.month.service.friend.dto.response;

import com.month.domain.member.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FriendMemberInfoResponse {

	private final Long id;

	private final String email;

	private final String name;

	private final String photoUrl;

	private final boolean isFavorite;

	public static FriendMemberInfoResponse of(Member member, Boolean isFavorite) {
		return new FriendMemberInfoResponse(member.getId(), member.getEmail(), member.getName(), member.getPhotoUrl(), isFavorite);
	}

}
