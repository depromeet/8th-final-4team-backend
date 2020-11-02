package com.month.service.friend;

import com.month.domain.friend.FriendMapper;
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

	static FriendMapper findFriendMapper(FriendMapperRepository friendMapperRepository, Long memberId, Long targetMemberId) {
		FriendMapper friendMapper = friendMapperRepository.findFriendMapper(memberId, targetMemberId);
		if (friendMapper == null) {
			throw new IllegalArgumentException(String.format("멤버 (%s)는 멤버 (%s)에게 친구로 등록된 멤버가 아닙니다.", targetMemberId, memberId));
		}
		return friendMapper;
	}

}
