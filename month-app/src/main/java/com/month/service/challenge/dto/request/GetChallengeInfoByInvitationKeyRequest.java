package com.month.service.challenge.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class GetChallengeInfoByInvitationKeyRequest {

	@NotBlank
	private String invitationKey;

}
