package com.month.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ApiResponse<T> {

	public final static ApiResponse<String> OK = new ApiResponse<>("", "", "OK");

	private final String code;
	private final String message;
	private final T data;

	public static <T> ApiResponse<T> of(T data) {
		return new ApiResponse<>("", "", data);
	}

}