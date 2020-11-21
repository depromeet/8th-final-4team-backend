package com.month.utils.jwt;

import com.month.utils.jwt.dto.SignUpToken;

public interface JwtTokenProvider {

	String encodeSignUpToken(String idToken, String email);

	SignUpToken decodeSignUpToken(String token);

}
