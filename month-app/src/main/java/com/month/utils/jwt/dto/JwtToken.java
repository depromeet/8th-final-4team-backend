package com.month.utils.jwt.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class JwtToken {

	private String idToken;

	private String email;

	private JwtToken(String idToken, String email) {
		this.idToken = idToken;
		this.email = email;
	}

	public static JwtToken newInstance(String idToken, String email) {
		return new JwtToken(idToken, email);
	}

}
