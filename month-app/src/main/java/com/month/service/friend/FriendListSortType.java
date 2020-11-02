package com.month.service.friend;

import com.month.service.friend.dto.response.FriendMemberInfoResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public enum FriendListSortType {

	NAME("NAME", Comparator.comparing(FriendMemberInfoResponse::getName)),
	EMAIL("EMAIL", Comparator.comparing(FriendMemberInfoResponse::getEmail));

	private final String field;
	private final Comparator<FriendMemberInfoResponse> comparator;

	private static Map<String, FriendListSortType> cachingFriendListSortType = new HashMap<>();

}
