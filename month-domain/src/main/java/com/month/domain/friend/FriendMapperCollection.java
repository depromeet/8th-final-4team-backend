package com.month.domain.friend;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FriendMapperCollection {

	private final List<FriendMapper> friendMapperList = new ArrayList<>();

	private final Map<Long, FriendMapper> friendMapperMap = new HashMap<>();

	private FriendMapperCollection(List<FriendMapper> friendMapperList) {
		this.friendMapperList.addAll(friendMapperList);
		this.friendMapperMap.putAll(friendMapperList.stream()
				.collect(Collectors.toMap(FriendMapper::getTargetMemberId, friendMapper -> friendMapper)));
	}

	public static FriendMapperCollection of(List<FriendMapper> friendMappers) {
		return new FriendMapperCollection(friendMappers);
	}

	public List<Long> getFriendsIds() {
		return this.friendMapperList.stream()
				.map(FriendMapper::getTargetMemberId)
				.collect(Collectors.toList());
	}

	public FriendMapper getFriendMapperByFriendId(Long friendId) {
		return friendMapperMap.get(friendId);
	}

}