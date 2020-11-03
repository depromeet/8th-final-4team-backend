package com.month.controller;

import com.month.config.i18n.Translator;
import com.month.exception.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.month.exception.type.ExceptionType.*;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice(basePackages = "com.month.controller")
public class ExceptionControllerAdvice {

	private final Translator translator;

	@ExceptionHandler(InvalidSessionException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ApiResponse<Object> handleInvalidSessionException(InvalidSessionException e) {
		log.error(e.getMessage(), e);
		return new ApiResponse<>(INVALID_SESSION_EXCEPTION.getCode(),
				translator.toLocale(INVALID_SESSION_EXCEPTION.getMessage()),
				e.getData());
	}

	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ApiResponse<Object> handleInternalServerException(IllegalArgumentException e) {
		log.error(e.getMessage(), e);
		return new ApiResponse<>(INTERNAL_SERVER_EXCEPTION.getCode(),
				translator.toLocale(INTERNAL_SERVER_EXCEPTION.getMessage()),
				null);
	}

	@ExceptionHandler(NotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ApiResponse<Object> handleNotFoundException(NotFoundException e) {
		log.error(e.getMessage(), e);
		return new ApiResponse<>(NOT_FOUND_EXCEPTION.getCode(),
				translator.toLocale(NOT_FOUND_EXCEPTION.getMessage(), translator.toLocale(e.getDescription().getMessage())),
				e.getData());
	}

	@ExceptionHandler(ConflictException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public ApiResponse<Object> handleConflictException(ConflictException e) {
		log.error(e.getMessage(), e);
		return new ApiResponse<>(CONFLICT_EXCEPTION.getCode(),
				translator.toLocale(CONFLICT_EXCEPTION.getMessage(), translator.toLocale(e.getDescription().getMessage())),
				e.getData());
	}

	@ExceptionHandler(NotAllowedException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public ApiResponse<Object> handleNotAllowedException(NotAllowedException e) {
		log.error(e.getMessage(), e);
		return new ApiResponse<>(NOT_ALLOWED_EXCEPTION.getCode(),
				translator.toLocale(NOT_ALLOWED_EXCEPTION.getMessage(), translator.toLocale(e.getDescription().getMessage())),
				e.getData());
	}

	@ExceptionHandler(ValidationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiResponse<Object> handleBadRequestException(ValidationException e) {
		log.error(e.getMessage(), e);
		return new ApiResponse<>(VALIDATION_EXCEPTION.getCode(),
				translator.toLocale(VALIDATION_EXCEPTION.getMessage(), translator.toLocale(e.getDescription().getMessage())),
				e.getData());
	}

}
