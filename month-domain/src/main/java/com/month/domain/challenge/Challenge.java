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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

	private String color; // TODO enum vs #686868 프론트쪽과 협의 필요

	@Embedded
	private DateTimeInterval dateTimeInterval;

	private int membersCount;

	@OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ChallengeMemberMapper> challengeMemberMappers = new ArrayList<>();

	public String getUuid() {
		return this.uuid.getUuid();
	}

	public LocalDateTime getStartDateTime() {
		return this.dateTimeInterval.getStartDateTime();
	}

	public LocalDateTime getEndDateTime() {
		return this.dateTimeInterval.getEndDateTime();
	}

	int calculateProgressDays() {
		final LocalDateTime now = LocalDateTime.now();
		// 이미 끝난 챌린지의 경우에는 startDateTime ~ endDateTime
		if (isFinishChallenge(now)) {
			Period period = Period.between(getStartDate(), getEndDate());
			return period.getDays();
		}
		// 현재 진행중인 챌린지의 경우에는 startDateTime ~ now
		Period period = Period.between(getStartDate(), now.toLocalDate());
		return period.getDays();
	}

	boolean isFinishChallenge(LocalDateTime datetime) {
		return this.getEndDateTime().isBefore(datetime);
	}

	private LocalDate getStartDate() {
		return getStartDateTime().toLocalDate();
	}

	private LocalDate getEndDate() {
		return getEndDateTime().toLocalDate();
	}

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
		this.membersCount += collect.size();
	}

	public void addCreator(Long memberId) {
		this.challengeMemberMappers.add(ChallengeMemberMapper.creator(this, memberId));
		this.membersCount++;
	}

	public void addParticipator(Long memberId) {
		this.challengeMemberMappers.add(ChallengeMemberMapper.participator(this, memberId));
		this.membersCount++;
	}

	public List<Long> getMemberIds() {
		return this.challengeMemberMappers.stream()
				.map(ChallengeMemberMapper::getMemberId)
				.collect(Collectors.toList());
	}

}
