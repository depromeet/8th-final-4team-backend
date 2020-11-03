package com.month.domain.friend.repository;

import com.month.domain.friend.FriendMapper;

import java.util.List;

public interface FriendMapperRepositoryCustom {

	FriendMapper findFriendMapper(Long memberId, Long targetMemberId);

	List<FriendMapper> findAllByMemberId(Long memberId);

}
