package com.month.domain.challenge;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class InvitationKeyTest {

	@Test
	void 새로운_초대키가_생성된다() {
		// given
		String challenge_uuid = "uuid";
		Long memberId = 1L;

		// when
		InvitationKey invitationKey = InvitationKey.newInstance(challenge_uuid, memberId);

		// then
		assertThat(invitationKey.getInvitationKey()).isNotNull();
		assertThat(invitationKey.getInvitationKey()).startsWith(challenge_uuid);
		assertThat(invitationKey.getInvitationKey()).endsWith(String.valueOf(memberId));
	}

	@Test
	void 초대키의_만료시간은_7일이다() {
		// when
		InvitationKey invitationKey = InvitationKey.newInstance("uuid", 1L);

		// then
		LocalDateTime now = LocalDateTime.now();
		assertThat(invitationKey.getExpireDateTime()).isBetween(now.plusDays(7).minusMinutes(1), now.plusDays(7).plusMinutes(1));
	}

}
