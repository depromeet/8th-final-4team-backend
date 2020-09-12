package com.month.domain.common;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UuidTest {

	@Test
	void 새로운_유니크한_UUID_가_생성된다() {
		// when
		Uuid uuid = Uuid.newInstance();

		// then
		assertThat(uuid.getUuid()).isNotNull();
		assertThat(uuid.getUuid().length()).isEqualTo(39);
	}

}
