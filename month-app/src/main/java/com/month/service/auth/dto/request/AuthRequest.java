package com.month.service.auth.dto.request;

import com.month.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class AuthRequest {

	@NotBlank
	private String idToken;

	@NotBlank
	private String email;

	private String name;

	private String photoUrl;

	private String providerId;

	@Builder(builderClassName = "TestBuilder", builderMethodName = "testBuilder")
	public AuthRequest(String idToken, String email, String name, String photoUrl, String providerId) {
		this.idToken = idToken;
		this.email = email;
		this.name = name;
		this.photoUrl = photoUrl;
		this.providerId = providerId;
	}

	public Member toEntity() {
		return Member.newInstance(email, name, photoUrl, providerId, idToken);
	}

}
