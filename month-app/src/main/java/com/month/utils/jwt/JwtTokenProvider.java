package com.month.utils.jwt;

import com.month.utils.jwt.dto.JwtToken;

public interface JwtTokenProvider {

	String createToken(String idToken, String email);

	JwtToken decodeToken(String token);

}
