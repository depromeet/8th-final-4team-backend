package com.month.domain.common;

import com.month.exception.ValidationException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DateTimeIntervalTest {

	@MethodSource("sources_date_time_interval")
	@ParameterizedTest
	void 시작날짜가_종료날짜보다_이후이면_에러가_발생한다() {
		// given
		LocalDateTime startDateTime = LocalDateTime.of(2020, 10, 5, 0, 0);
		LocalDateTime endDateTime = LocalDateTime.of(2020, 9, 5, 0, 0);

		// when
		assertThatThrownBy(() -> {
			DateTimeInterval.of(startDateTime, endDateTime);
		}).isInstanceOf(ValidationException.class);
	}

	private static Stream<Arguments> sources_date_time_interval() {
		return Stream.of(
				Arguments.of(LocalDateTime.of(2020, 10, 5, 0, 0), LocalDateTime.of(2020, 9, 5, 0, 0)),
				Arguments.of(LocalDateTime.of(2020, 9, 1, 0, 1), LocalDateTime.of(2020, 9, 1, 0, 0))
		);
	}

}
