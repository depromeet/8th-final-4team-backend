package com.month.exception;

import com.month.exception.type.ExceptionDescriptionType;
import com.month.exception.common.CustomMessageException;

public class ConflictException extends CustomMessageException {

	public ConflictException(String message, ExceptionDescriptionType description) {
		super(message, description, null);
	}

}
