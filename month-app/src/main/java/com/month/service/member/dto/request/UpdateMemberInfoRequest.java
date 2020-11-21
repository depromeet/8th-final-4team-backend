package com.month.service.member.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateMemberInfoRequest {

	private String name;

	private String photoUrl;

	private UpdateMemberInfoRequest(String name, String photoUrl) {
		this.name = name;
		this.photoUrl = photoUrl;
	}

	public static UpdateMemberInfoRequest testInstance(String name, String photoUrl) {
		return new UpdateMemberInfoRequest(name, photoUrl);
	}

}
