package com.month.exception.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExceptionDescriptionType {

	MEMBER("member"),
	MEMBER_IN_CHALLENGE("member_in_challenge"),
	FILE("file"),
	CHALLENGE("challenge"),
	INVITATION_KEY("invitation_key"),
	FRIEND("friend"),
	EMAIL("email"),
	DATE_TIME("date_time"),
	REGISTER_FRIEND("register_friend"),
	TOKEN("token");

	private final String message;
}
