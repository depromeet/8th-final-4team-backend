package com.month.domain.challenge;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class InvitationKey {

	private final static String DELIMITER = "-";
	private final static int EXPIRE_DAY = 7;

	private String invitationKey;

	private LocalDateTime expireDateTime;

	private InvitationKey(String invitationKey, LocalDateTime expireDateTime) {
		this.invitationKey = invitationKey;
		this.expireDateTime = expireDateTime;
	}

	static InvitationKey newInstance(String challengeUuid, Long memberId) {
		return new InvitationKey(createInvitationKey(challengeUuid, memberId), calculateExpiresDateTime());
	}

	private static String createInvitationKey(String challengeUuid, Long memberId) {
		return createIdentification() + DELIMITER + challengeUuid + DELIMITER + memberId;
	}

	private static LocalDateTime calculateExpiresDateTime() {
		return LocalDateTime.now().plusDays(EXPIRE_DAY);
	}

	private static String createIdentification() {
		return UUID.randomUUID().toString().substring(0, 5);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		InvitationKey that = (InvitationKey) o;
		return Objects.equals(invitationKey, that.invitationKey);
	}

	@Override
	public int hashCode() {
		return Objects.hash(invitationKey);
	}

}
