package com.month.exception.common;

import lombok.Getter;

@Getter
public abstract class CustomException extends RuntimeException {

	protected Object data;

	public CustomException(String message, Object data) {
		super(message);
		this.data = data;
	}

}
