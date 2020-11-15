package com.month.service.friend.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class RetrieveFriendDetailRequest {

	@NotNull
	private Long friendId;

	private RetrieveFriendDetailRequest(Long friendId) {
		this.friendId = friendId;
	}

	public static RetrieveFriendDetailRequest testInstance(Long friendId) {
		return new RetrieveFriendDetailRequest(friendId);
	}

}
