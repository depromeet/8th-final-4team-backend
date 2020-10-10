package com.month.domain.challenge;

import com.month.domain.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ChallengePlan extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private String description;

	private String color;

	private int period;

	private int maxMembersCount;

	private int currentMembersCount;

	private String invitationKey; // TODO Convert To VO

	@OneToMany(mappedBy = "challengePlan", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ChallengePlanMemberMapper> challengePlanMemberMapperList = new ArrayList<>();

	private boolean isActive;

	@Builder
	public ChallengePlan(String name, String description, String color, int period, int maxMembersCount) {
		this.name = name;
		this.description = description;
		this.color = color;
		this.period = period;
		this.maxMembersCount = maxMembersCount;
		this.currentMembersCount = 0;
		this.isActive = true;
	}

	public static ChallengePlan newInstance(String name, String description, String color, int period, int maxMembersCount) {
		return new ChallengePlan(name, description, color, period, maxMembersCount);
	}

	public void addCreator(Long memberId) {
		this.challengePlanMemberMapperList.add(ChallengePlanMemberMapper.creator(this, memberId));
	}

	public Challenge convertToChallenge() {
		final LocalDateTime now = LocalDateTime.now();
		Challenge challenge = Challenge.newInstance(name, description, color, now, now.plusDays(period));
		challenge.addMembers(this.challengePlanMemberMapperList.stream()
				.map(challengePlanMemberMapper -> challengePlanMemberMapper.convertChallengeMemberMapper(challenge))
				.collect(Collectors.toList()));
		return challenge;
	}

	public void inactiveChallengePlan() {
		this.isActive = false;
	}

	public void validateCreator(Long memberId) {
		if (!isCreator(memberId)) {
			throw new IllegalArgumentException(String.format("회원 (%s) 는 생성자가 아닙니다", memberId));
		}
	}

	private boolean isCreator(Long memberId) {
		return this.challengePlanMemberMapperList.stream()
				.anyMatch(challengePlanMemberMapper -> challengePlanMemberMapper.isCreator(memberId));
	}

	public void addParticipator(Long memberId) {
		this.challengePlanMemberMapperList.add(ChallengePlanMemberMapper.participator(this, memberId));
	}
}
