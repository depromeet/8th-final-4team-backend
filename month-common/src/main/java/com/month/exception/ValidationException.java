package com.month.exception;

import com.month.exception.type.ExceptionDescriptionType;
import com.month.exception.common.CustomMessageException;

public class ValidationException extends CustomMessageException {

	public ValidationException(String message, ExceptionDescriptionType description) {
		super(message, description, null);
	}

}
