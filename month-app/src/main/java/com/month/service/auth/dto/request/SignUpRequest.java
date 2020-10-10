package com.month.service.auth.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class SignUpRequest {

	@NotBlank
	private String signUpToken;

	@NotBlank
	private String name;

	@NotBlank
	private String photoUrl;

	@Builder(builderClassName = "TestBuilder", builderMethodName = "testBuilder")
	public SignUpRequest(String signUpToken, String name, String photoUrl) {
		this.signUpToken = signUpToken;
		this.name = name;
		this.photoUrl = photoUrl;
	}

}
