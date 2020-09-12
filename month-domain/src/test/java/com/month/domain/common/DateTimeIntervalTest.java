package com.month.domain.common;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DateTimeIntervalTest {

	@Test
	void 시작날짜가_종료날짜보다_이전일경우_정상작동한다() {
		// given
		LocalDateTime startDateTime = LocalDateTime.of(2020, 8, 1, 0, 0);
		LocalDateTime endDateTime = LocalDateTime.of(2020, 9, 1, 0, 0);

		// when
		DateTimeInterval dateTimeInterval = DateTimeInterval.of(startDateTime, endDateTime);

		// then
		assertThat(dateTimeInterval.getStartDateTime()).isEqualTo(startDateTime);
		assertThat(dateTimeInterval.getEndDateTime()).isEqualTo(endDateTime);
	}

	@Test
	void 시작날짜가_종료날짜보다_이일경우_정상작동한다() {
		// given
		LocalDateTime startDateTime = LocalDateTime.of(2020, 9, 1, 0, 0);
		LocalDateTime endDateTime = LocalDateTime.of(2020, 8, 1, 0, 0);

		// when & then
		assertThatThrownBy(() -> {
			DateTimeInterval.of(startDateTime, endDateTime);
		}).isInstanceOf(IllegalArgumentException.class);
	}

}
