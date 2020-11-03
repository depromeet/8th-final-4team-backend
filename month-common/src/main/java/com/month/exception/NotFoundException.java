package com.month.exception;

import com.month.exception.type.ExceptionDescriptionType;
import com.month.exception.common.CustomMessageException;

public class NotFoundException extends CustomMessageException {

	public NotFoundException(String message, ExceptionDescriptionType description) {
		super(message, description, null);
	}

}
