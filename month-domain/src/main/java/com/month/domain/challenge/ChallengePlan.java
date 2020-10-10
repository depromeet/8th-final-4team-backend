package com.month.domain.challenge;

import com.month.domain.BaseTimeEntity;
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

	@Embedded
	private InvitationKey invitationKey;

	@OneToMany(mappedBy = "challengePlan", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ChallengePlanMemberMapper> challengePlanMemberMapperList = new ArrayList<>();

	private boolean isActive;

	public String getInvitationKey() {
		return this.invitationKey.getInvitationKey();
	}

	@Builder
	public ChallengePlan(String name, String description, String color, int period, int maxMembersCount) {
		this.name = name;
		this.description = description;
		this.color = color;
		this.period = period;
		this.maxMembersCount = maxMembersCount;
		this.currentMembersCount = 0;
		this.isActive = true;
		this.invitationKey = InvitationKey.newInstance();
	}

	public static ChallengePlan newInstance(String name, String description, String color, int period, int maxMembersCount) {
		return new ChallengePlan(name, description, color, period, maxMembersCount);
	}

	public void addCreator(Long memberId) {
		if (isMember(memberId)) {
			throw new IllegalArgumentException(String.format("멤버 (%s)는 이미 챌린지 (%s)에 참여하고 있습니다.", memberId, this.id));
		}
		this.challengePlanMemberMapperList.add(ChallengePlanMemberMapper.creator(this, memberId));
		this.currentMembersCount++;
	}

	public void addParticipator(Long memberId) {
		if (isMember(memberId)) {
			throw new IllegalArgumentException(String.format("멤버 (%s)는 이미 챌린지 (%s)에 참여하고 있습니다.", memberId, this.id));
		}
		this.challengePlanMemberMapperList.add(ChallengePlanMemberMapper.participator(this, memberId));
		this.currentMembersCount++;
	}

	public void validateCreator(Long memberId) {
		if (!isCreator(memberId)) {
			throw new IllegalArgumentException(String.format("회원 (%s) 는 챌린지 (%s) 의 생성자가 아닙니다", memberId, id));
		}
	}

	private boolean isCreator(Long memberId) {
		return this.challengePlanMemberMapperList.stream()
				.anyMatch(challengePlanMemberMapper -> challengePlanMemberMapper.isCreator(memberId));
	}

	public void validateIsMember(Long memberId) {
		if (!isMember(memberId)) {
			throw new IllegalArgumentException(String.format("회원 (%s) 는 챌린지 (%s) 에 참가 하고 있지 않습니다", memberId, id));
		}
	}

	private boolean isMember(Long memberId) {
		return this.challengePlanMemberMapperList.stream()
				.anyMatch(challengePlanMemberMapper -> challengePlanMemberMapper.isMember(memberId));
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

	public void refreshInvitationKey(Long memberId) {
		if (!isCreator(memberId)) {
			throw new IllegalArgumentException(String.format("멤버 (%s) 는 챌린지 (%s) 의 생성자가 아닙니다", memberId, this.id));
		}
		this.invitationKey = InvitationKey.newInstance();
	}

}
