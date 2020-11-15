package com.month.exception;

import com.month.exception.common.CustomException;

public class InvalidSessionException extends CustomException {

	public InvalidSessionException(String message) {
		super(message, null);
	}

}
