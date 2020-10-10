package com.month.domain.challenge;

import com.month.domain.BaseTimeEntity;
import com.month.domain.common.DateTimeInterval;
import com.month.domain.common.Uuid;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Challenge extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Embedded
	private Uuid uuid;

	private String name;

	private String description;

	private String color;

	@Embedded
	private DateTimeInterval dateTimeInterval;

	private int membersCount;

	@OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ChallengeMemberMapper> challengeMemberMappers = new ArrayList<>();

	@Builder
	public Challenge(String name, String description, String color, LocalDateTime startDateTime, LocalDateTime endDateTime) {
		this.uuid = Uuid.newInstance();
		this.name = name;
		this.description = description;
		this.color = color;
		this.dateTimeInterval = DateTimeInterval.of(startDateTime, endDateTime);
		this.membersCount = 0;
	}

	static Challenge newInstance(String name, String description, String color, LocalDateTime startDateTime, LocalDateTime endDateTime) {
		return Challenge.builder()
				.name(name)
				.description(description)
				.color(color)
				.startDateTime(startDateTime)
				.endDateTime(endDateTime)
				.build();
	}

	void addMembers(List<ChallengeMemberMapper> collect) {
		this.challengeMemberMappers.addAll(collect);
	}

}
