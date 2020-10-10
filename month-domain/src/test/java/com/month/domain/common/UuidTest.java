package com.month.domain.common;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UuidTest {

	@Test
	void UUID_생성() {
		// when
		Uuid uuid = Uuid.newInstance();

		// then
		assertThat(uuid.getUuid()).isNotEmpty();
		assertThat(uuid.getUuid()).startsWith("v1");
	}

}
