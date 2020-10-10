package com.month.utils.jwt.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignUpToken {

	private String idToken;

	private String email;

	private SignUpToken(String idToken, String email) {
		this.idToken = idToken;
		this.email = email;
	}

	public static SignUpToken newInstance(String idToken, String email) {
		return new SignUpToken(idToken, email);
	}

}
