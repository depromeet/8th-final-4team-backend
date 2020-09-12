package com.month.service.member.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberUpdateInfoRequest {

	private String name;

	private String photoUrl;

	private MemberUpdateInfoRequest(String name, String photoUrl) {
		this.name = name;
		this.photoUrl = photoUrl;
	}

	public static MemberUpdateInfoRequest testInstance(String name, String photoUrl) {
		return new MemberUpdateInfoRequest(name, photoUrl);
	}

}
