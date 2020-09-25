package com.month.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UploadType {

	PROFILE("profile"),
	CHALLENGE("challenge");

	private final String directory;

}
