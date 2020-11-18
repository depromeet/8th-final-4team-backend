package com.month.domain.challenge;

import com.month.domain.BaseTimeEntity;
import com.month.domain.common.DateTimeInterval;
import com.month.domain.common.Uuid;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
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

	@Enumerated(EnumType.STRING)
	private ChallengeType type;

	private String color;

	@Embedded
	private DateTimeInterval dateTimeInterval;

	private int membersCount;

	@Embedded
	private InvitationKey invitationKey;

	@OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ChallengeMemberMapper> challengeMemberMappers = new ArrayList<>();

	@Builder
	public Challenge(String name, ChallengeType type, String color, LocalDateTime startDateTime, LocalDateTime endDateTime) {
		this.uuid = Uuid.newInstance();
		this.name = name;
		this.type = type;
		this.color = color;
		this.dateTimeInterval = DateTimeInterval.of(startDateTime, endDateTime);
		this.membersCount = 0;
		this.invitationKey = InvitationKey.newInstance();
	}

	public static Challenge newInstance(String name, ChallengeType type, String color, LocalDateTime startDateTime, LocalDateTime endDateTime) {
		return Challenge.builder()
				.name(name)
				.type(type)
				.color(color)
				.startDateTime(startDateTime)
				.endDateTime(endDateTime)
				.build();
	}

	private String getInvitationKey() {
		return this.invitationKey.getInvitationKey();
	}

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
		if (isDone()) {
			Period period = Period.between(getStartDate(), getEndDate());
			return period.getDays();
		}
		// 현재 진행중인 챌린지의 경우에는 startDateTime ~ now
		Period period = Period.between(getStartDate(), now.toLocalDate());
		return period.getDays();
	}

	private LocalDate getStartDate() {
		return getStartDateTime().toLocalDate();
	}

	private LocalDate getEndDate() {
		return getEndDateTime().toLocalDate();
	}

	public void addCreator(Long memberId) {
		this.challengeMemberMappers.add(ChallengeMemberMapper.creator(this, memberId));
		this.membersCount++;
	}

	boolean isDone() {
		final LocalDateTime now = LocalDateTime.now();
		return this.getEndDateTime().isBefore(now);
	}

	boolean isDoing() {
		final LocalDateTime now = LocalDateTime.now();
		return getEndDateTime().isAfter(now) && getStartDateTime().isBefore(now);
	}

	boolean isTodo() {
		final LocalDateTime now = LocalDateTime.now();
		return getStartDateTime().isAfter(now);
	}

	public String issueInvitationKey(Long memberId) {
		validateApprovedMember(memberId);
		validateTodoChallenge();
		return getInvitationKey();
	}

	private void validateTodoChallenge() {
		if (!isTodo()) {
			throw new IllegalArgumentException(String.format("이미 시작한 챌린지 (%s) 에 대해서는 초대키를 반환할 수 없습니다", uuid));
		}
	}

	private void validateApprovedMember(Long memberId) {
		if (!isApprovedMemberInChallenge(memberId)) {
			throw new IllegalArgumentException(String.format("해당 멤버 (%s) 는 챌린지 (%s) 에 참가하고 있지 않습니다", memberId, uuid));
		}
	}

	private boolean isApprovedMemberInChallenge(Long memberId) {
		return challengeMemberMappers.stream()
				.anyMatch(challengeMemberMapper -> challengeMemberMapper.isApprovedMember(memberId));
	}

}
