package com.month.service.friend.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class CreateFriendMapperRequest {

	@NotBlank
	private String email;

	private CreateFriendMapperRequest(String email) {
		this.email = email;
	}

	public static CreateFriendMapperRequest testInstance(String email) {
		return new CreateFriendMapperRequest(email);
	}

}
