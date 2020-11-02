package com.month.service.friend;

import com.month.domain.friend.FriendMapperRepository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class FriendMapperServiceUtils {

	static void validateNonFriendMapper(FriendMapperRepository friendMapperRepository, Long memberId, Long targetMemberId) {
		if (friendMapperRepository.findFriendMapper(memberId, targetMemberId) != null) {
			throw new IllegalArgumentException(String.format("멤버 (%s)에게 이미 친구 추가된 멤버 (%s) 입니다.", memberId, targetMemberId));
		}
	}

}
