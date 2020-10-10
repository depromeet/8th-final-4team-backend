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

	private final String token;

	private final String name;

	private final String photoUrl;

	private final String sessionId;

	public static AuthResponse signUp(String token, String name, String photoUrl) {
		return new AuthResponse(AuthType.SIGN_UP, token, name, photoUrl, null);
	}

	public static AuthResponse login(String sessionId) {
		return new AuthResponse(AuthType.LOGIN, null, null, null, sessionId);
	}

}
