package com.month.domain.member;

import com.month.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Member extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Embedded
	private Email email;

	private String name;

	private String photoUrl;

	private String providerId;

	@Column(nullable = false)
	private String idToken;

	@Builder
	private Member(String email, String name, String photoUrl, String providerId, String idToken) {
		this.email = Email.of(email);
		this.name = name;
		this.photoUrl = photoUrl;
		this.providerId = providerId;
		this.idToken = idToken;
	}

	public static Member newInstance(String email, String name, String photoUrl, String providerId, String idToken) {
		return new Member(email, name, photoUrl, providerId, idToken);
	}

	public String getEmail() {
		return this.email.getEmail();
	}

	public void updateInfo(String name, String photoUrl) {
		if (StringUtils.hasText(name)) {
			this.name = name;
		}
		if (StringUtils.hasText(photoUrl)) {
			this.photoUrl = photoUrl;
		}
	}

}
