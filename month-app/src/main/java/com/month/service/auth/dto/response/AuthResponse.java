package com.month.service.auth.dto.response;

import com.month.type.AuthType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthResponse {

	private final AuthType type;

	private final String signUpToken;

	private final String name;

	private final String photoUrl;

	private final String loginSessionId;

	public static AuthResponse signUp(String signUpToken, String name, String photoUrl) {
		return new AuthResponse(AuthType.SIGN_UP, signUpToken, name, photoUrl, null);
	}

	public static AuthResponse login(String loginSessionId) {
		return new AuthResponse(AuthType.LOGIN, null, null, null, loginSessionId);
	}

}
