package com.month.domain.member;

public final class MemberCreator {

	public static Member create(String email) {
		return Member.builder()
				.email(email)
				.name("강승호")
				.idToken("idToken")
				.photoUrl("photoUrl")
				.providerId("providerId")
				.build();
	}

	public static Member create(String email, String name, String idToken) {
		return Member.builder()
				.email(email)
				.name(name)
				.idToken(idToken)
				.photoUrl("photoUrl")
				.providerId("providerId")
				.build();
	}

	public static Member create(String email, String name, String photoUrl, String providerId) {
		return Member.builder()
				.email(email)
				.name(name)
				.photoUrl(photoUrl)
				.providerId(providerId)
				.idToken("idToken")
				.build();
	}

}