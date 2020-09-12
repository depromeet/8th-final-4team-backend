package com.month.utils;

public final class HeaderUtils {

	public final static String BEARER_TOKEN = "Bearer ";

	public static void validateAvailableHeader(String header) {
		if (header == null) {
			throw new IllegalArgumentException("세션이 없습니다");
		}
		if (!header.startsWith(BEARER_TOKEN)) {
			throw new IllegalArgumentException(String.format("잘못된 세션입니다 (%s)", header));
		}
	}

}
