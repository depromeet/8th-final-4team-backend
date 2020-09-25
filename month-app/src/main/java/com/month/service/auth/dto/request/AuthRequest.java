package com.month.service.auth.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class AuthRequest {

	@NotBlank
	private String idToken;

	@Builder(builderClassName = "TestBuilder", builderMethodName = "testBuilder")
	public AuthRequest(String idToken) {
		this.idToken = idToken;
	}

}
