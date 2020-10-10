package com.month.domain.member;

public final class MemberCreator {

	public static Member create(String email, String name, String photoUrl, String uid) {
		return Member.builder()
				.email(email)
				.name(name)
				.photoUrl(photoUrl)
				.uid(uid)
				.build();
	}

	public static Member create(String email) {
		return Member.builder()
				.email(email)
				.uid("uid")
				.build();
	}

}