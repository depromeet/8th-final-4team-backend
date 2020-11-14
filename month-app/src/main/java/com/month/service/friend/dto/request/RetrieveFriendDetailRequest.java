package com.month.service.friend.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RetrieveFriendDetailRequest {

	private Long friendId;

	public RetrieveFriendDetailRequest(Long friendId) {
		this.friendId = friendId;
	}

	public static RetrieveFriendDetailRequest testInstance(Long friendId) {
		return new RetrieveFriendDetailRequest(friendId);
	}

}
