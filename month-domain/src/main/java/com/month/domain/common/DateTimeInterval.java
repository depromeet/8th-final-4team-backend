package com.month.domain.common;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class DateTimeInterval {

	private LocalDateTime startDateTime;

	private LocalDateTime endDateTime;

	private DateTimeInterval(LocalDateTime startDateTime, LocalDateTime endDateTime) {
		this.startDateTime = startDateTime;
		this.endDateTime = endDateTime;
	}

	public static DateTimeInterval of(LocalDateTime startDateTime, LocalDateTime endDateTime) {
		validateDateTime(startDateTime, endDateTime);
		return new DateTimeInterval(startDateTime, endDateTime);
	}

	private static void validateDateTime(LocalDateTime startDateTime, LocalDateTime endDateTime) {
		if (startDateTime.isAfter(endDateTime)) {
			throw new IllegalArgumentException(String.format("종료시간 (%s)이 시작시간 (%s) 이전일 수 업습니다.", startDateTime, endDateTime));
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		DateTimeInterval that = (DateTimeInterval) o;
		return Objects.equals(startDateTime, that.startDateTime) &&
				Objects.equals(endDateTime, that.endDateTime);
	}

	@Override
	public int hashCode() {
		return Objects.hash(startDateTime, endDateTime);
	}

}
