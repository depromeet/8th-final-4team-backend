package com.month.exception.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExceptionType {

	INVALID_SESSION_EXCEPTION("INVALID_SESSION_EXCEPTION", "invalid_session_exception"),
	NOT_FOUND_EXCEPTION("NOT_FOUND_EXCEPTION", "not_found_exception"),
	CONFLICT_EXCEPTION("CONFLICT_EXCEPTION", "conflict_exception"),
	NOT_ALLOWED_EXCEPTION("NOT_ALLOWED_EXCEPTION", "not_allowed_exception"),
	INTERNAL_SERVER_EXCEPTION("INTERNAL_SERVER_EXCEPTION", "internal_server_exception"),
	VALIDATION_EXCEPTION("VALIDATION_EXCEPTION", "validation_exception"),
	REQUEST_VALIDATION_EXCEPTION("REQUEST_VALIDATION_EXCEPTION", null);

	private final String code;
	private final String message;

}
