package com.month.domain.challenge;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class InvitationKey {

	private String invitationKey;

	private InvitationKey(String invitationKey) {
		this.invitationKey = invitationKey;
	}

	public static InvitationKey newInstance() {
		return new InvitationKey(UUID.randomUUID().toString().substring(0, 18));
	}

}
