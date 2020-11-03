package com.month.service.friend;

import com.month.service.friend.dto.response.FriendMemberInfoResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Comparator;

@Getter
@RequiredArgsConstructor
public enum FriendListSortType {

	NAME("NAME", Comparator.comparing(FriendMemberInfoResponse::getName)),
	NAME_REVERSE("NAME_REVERSE", Comparator.comparing(FriendMemberInfoResponse::getName).reversed()),
	CREATED("CREATED", Comparator.comparing(FriendMemberInfoResponse::getCreatedDateTime)),
	CREATED_REVERSE("CREATED_REVERSE", Comparator.comparing(FriendMemberInfoResponse::getCreatedDateTime).reversed());

	private final String field;
	private final Comparator<FriendMemberInfoResponse> comparator;

}
