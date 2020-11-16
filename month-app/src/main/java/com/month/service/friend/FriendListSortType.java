package com.month.service.friend;

import com.month.service.friend.dto.response.FriendSimpleInfoResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Comparator;

@Getter
@RequiredArgsConstructor
public enum FriendListSortType {

	NAME("NAME", Comparator.comparing(FriendSimpleInfoResponse::getName)),
	NAME_REVERSE("NAME_REVERSE", Comparator.comparing(FriendSimpleInfoResponse::getName).reversed()),
	CREATED("CREATED", Comparator.comparing(FriendSimpleInfoResponse::getCreatedDateTime)),
	CREATED_REVERSE("CREATED_REVERSE", Comparator.comparing(FriendSimpleInfoResponse::getCreatedDateTime).reversed());

	private final String field;
	private final Comparator<FriendSimpleInfoResponse> comparator;

}
