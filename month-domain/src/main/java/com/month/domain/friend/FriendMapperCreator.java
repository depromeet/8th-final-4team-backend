package com.month.domain.friend;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FriendMapperCreator {

	public static FriendMapper create(Long memberId, Long targetMemberId) {
		return FriendMapper.builder()
				.memberId(memberId)
				.targetMemberId(targetMemberId)
				.isFavorite(false)
				.build();
	}

	public static FriendMapper create(Long memberId, Long targetMemberId, boolean isFavorite) {
		return FriendMapper.builder()
				.memberId(memberId)
				.targetMemberId(targetMemberId)
				.isFavorite(isFavorite)
				.build();
	}

}
