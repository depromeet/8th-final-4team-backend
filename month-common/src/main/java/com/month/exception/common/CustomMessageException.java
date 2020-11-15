package com.month.exception.common;

import com.month.exception.type.ExceptionDescriptionType;
import lombok.Getter;

@Getter
public abstract class CustomMessageException extends CustomException {

	private ExceptionDescriptionType description;

	public CustomMessageException(String message, ExceptionDescriptionType description, Object data) {
		super(message, data);
		this.description = description;
	}

}
