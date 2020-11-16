package com.month.service.friend.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class RetrieveFriendInfoRequest {

	@NotNull
	private Long friendId;

	private RetrieveFriendInfoRequest(Long friendId) {
		this.friendId = friendId;
	}

	public static RetrieveFriendInfoRequest testInstance(Long friendId) {
		return new RetrieveFriendInfoRequest(friendId);
	}

}
