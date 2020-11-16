package com.month.service.friend.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class RegisterFriendRequest {

	@NotBlank
	private String email;

	private RegisterFriendRequest(String email) {
		this.email = email;
	}

	public static RegisterFriendRequest testInstance(String email) {
		return new RegisterFriendRequest(email);
	}

}
