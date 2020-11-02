package com.month.service.friend.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateFriendMapperRequest {

	private String email;

	private CreateFriendMapperRequest(String email) {
		this.email = email;
	}

	public static CreateFriendMapperRequest testInstance(String email) {
		return new CreateFriendMapperRequest(email);
	}

}
