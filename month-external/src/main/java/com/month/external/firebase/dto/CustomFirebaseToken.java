package com.month.external.firebase.dto;

import com.google.firebase.auth.FirebaseToken;
import com.month.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CustomFirebaseToken {

	private String email;

	private String name;

	private String photoUrl;

	private String uid;

	@Builder
	public CustomFirebaseToken(String email, String name, String photoUrl, String uid) {
		validateFirebaseToken(email, uid);
		this.email = email;
		this.name = name;
		this.photoUrl = photoUrl;
		this.uid = uid;
	}

	private void validateFirebaseToken(String email, String uid) {
		if (email == null || uid == null) {
			throw new IllegalArgumentException(String.format("잘못된 파이어베이스 토큰 입니다. email: (%s), uid: (%s)", email, uid));
		}
	}

	public static CustomFirebaseToken of(FirebaseToken firebaseToken) {
		return CustomFirebaseToken.builder()
				.uid(firebaseToken.getUid())
				.email(firebaseToken.getEmail())
				.name(firebaseToken.getName())
				.photoUrl(firebaseToken.getPicture())
				.build();
	}

	public Member toEntity() {
		return Member.newInstance(email, name, photoUrl, uid);
	}

}
