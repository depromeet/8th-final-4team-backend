package com.month.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public final class LocalDateTimeUtils {

	private final static LocalTime customMinTime = LocalTime.of(0, 0, 0);
	private final static LocalTime customMaxTime = LocalTime.of(23, 59, 59);

	public static LocalDateTime convertToMaxOfLocalDateTime(LocalDate localDate) {
		return localDate.atTime(customMaxTime);
	}

	public static LocalDateTime convertToMinOfLocalDateTime(LocalDate localDate) {
		return localDate.atTime(customMinTime);
	}

}
