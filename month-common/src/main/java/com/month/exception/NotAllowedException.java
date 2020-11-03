package com.month.exception;

import com.month.exception.type.ExceptionDescriptionType;
import com.month.exception.common.CustomMessageException;

public class NotAllowedException extends CustomMessageException {

	public NotAllowedException(String message, ExceptionDescriptionType description) {
		super(message, description, null);
	}

}
