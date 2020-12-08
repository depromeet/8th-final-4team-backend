package com.month.domain.member;

import com.month.domain.common.Uuid;

public final class MemberCreator {

	public static Member create(String email, String name, String photoUrl, String uid) {
		return Member.builder()
				.email(email)
				.name(name)
				.photoUrl(photoUrl)
				.uid(uid)
				.build();
	}

	public static Member create(String email, String name) {
		return Member.builder()
				.email(email)
				.name(name)
				.uid("test" + Uuid.newInstance().getUuid())
				.build();
	}

	public static Member create(String email) {
		return Member.builder()
				.email(email)
				.uid("test" + Uuid.newInstance().getUuid())
				.name("testAccount")
				.build();
	}

}