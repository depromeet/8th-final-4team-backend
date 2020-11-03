package com.month.domain.common;

import com.month.exception.NotAllowedException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.time.LocalDateTime;

import static com.month.exception.type.ExceptionDescriptionType.DATE_TIME;

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
			throw new NotAllowedException(String.format("시작 날짜 (%s)가 종료 날짜 (%s) 보다 이후 일 수 없습니다", startDateTime, endDateTime), DATE_TIME);
		}
	}

}
