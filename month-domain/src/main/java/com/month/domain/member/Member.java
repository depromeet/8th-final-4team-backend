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

	@Column(nullable = false)
	private String uid;

	private String deviceToken;

	@Builder
	private Member(String email, String name, String photoUrl, String uid) {
		this.email = Email.of(email);
		this.name = name;
		this.photoUrl = photoUrl;
		this.uid = uid;
		this.deviceToken = null;
	}

	public static Member newInstance(String email, String name, String photoUrl, String uid) {
		return Member.builder()
				.email(email)
				.name(name)
				.photoUrl(photoUrl)
				.uid(uid)
				.build();
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

	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}
}
