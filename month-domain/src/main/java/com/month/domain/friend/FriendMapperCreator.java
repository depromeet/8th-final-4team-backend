package com.month.domain.friend;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FriendMapperCreator {

	public static FriendMapper create(Long memberId, Long targetMemberId) {
		return FriendMapper.builder()
				.memberId(memberId)
				.targetMemberId(targetMemberId)
				.build();
	}

}
