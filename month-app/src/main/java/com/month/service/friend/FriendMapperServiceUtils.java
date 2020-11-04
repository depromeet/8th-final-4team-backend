package com.month.service.friend;

import com.month.domain.friend.FriendMapper;
import com.month.domain.friend.FriendMapperRepository;
import com.month.exception.ConflictException;
import com.month.exception.NotFoundException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static com.month.exception.type.ExceptionDescriptionType.FRIEND;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class FriendMapperServiceUtils {

	static void validateNonFriendMapper(FriendMapperRepository friendMapperRepository, Long memberId, Long targetMemberId) {
		if (friendMapperRepository.findFriendMapper(memberId, targetMemberId) != null) {
			throw new ConflictException(String.format("멤버 (%s)에게 이미 친구 추가된 멤버 (%s) 입니다.", memberId, targetMemberId), FRIEND);
		}
	}

	static FriendMapper findFriendMapper(FriendMapperRepository friendMapperRepository, Long memberId, Long targetMemberId) {
		FriendMapper friendMapper = friendMapperRepository.findFriendMapper(memberId, targetMemberId);
		if (friendMapper == null) {
			throw new NotFoundException(String.format("멤버 (%s)는 멤버 (%s)에게 친구로 등록된 멤버가 아닙니다.", targetMemberId, memberId), FRIEND);
		}
		return friendMapper;
	}

}
