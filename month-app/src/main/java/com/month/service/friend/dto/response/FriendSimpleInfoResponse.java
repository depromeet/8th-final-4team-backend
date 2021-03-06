package com.month.service.friend.dto.response;

import com.month.domain.friend.FriendMapper;
import com.month.domain.member.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class FriendSimpleInfoResponse {

	private final Long id;

	private final String email;

	private final String name;

	private final String photoUrl;

	private final boolean isFavorite;

	private final LocalDateTime createdDateTime;

	public static FriendSimpleInfoResponse of(Member member, FriendMapper friendMapper) {
		return new FriendSimpleInfoResponse(member.getId(), member.getEmail(), member.getName(), member.getPhotoUrl(), friendMapper.isFavorite(), friendMapper.getCreatedDateTime());
	}

}
