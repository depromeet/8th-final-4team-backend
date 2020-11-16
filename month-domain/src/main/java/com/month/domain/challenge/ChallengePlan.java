package com.month.domain.challenge;

import com.month.domain.BaseTimeEntity;
import com.month.exception.ConflictException;
import com.month.exception.NotAllowedException;
import com.month.exception.NotFoundException;
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

import static com.month.exception.type.ExceptionDescriptionType.MEMBER_IN_CHALLENGE;

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
			throw new ConflictException(String.format("멤버 (%s)는 이미 챌린지 (%s)에 참여하고 있습니다.", memberId, this.id), MEMBER_IN_CHALLENGE);
		}
		this.challengePlanMemberMapperList.add(ChallengePlanMemberMapper.creator(this, memberId));
		this.currentMembersCount++;
	}

	public void addParticipator(Long memberId) {
		if (isMember(memberId)) {
			throw new ConflictException(String.format("멤버 (%s)는 이미 챌린지 (%s)에 참여하고 있습니다.", memberId, this.id), MEMBER_IN_CHALLENGE);
		}
		this.challengePlanMemberMapperList.add(ChallengePlanMemberMapper.participator(this, memberId));
		this.currentMembersCount++;
	}

	private void validateCreator(Long memberId) {
		if (!isCreator(memberId)) {
			throw new NotAllowedException(String.format("회원 (%s) 는 챌린지 (%s) 의 생성자가 아닙니다", memberId, id), MEMBER_IN_CHALLENGE);
		}
	}

	private boolean isCreator(Long memberId) {
		return this.challengePlanMemberMapperList.stream()
				.anyMatch(challengePlanMemberMapper -> challengePlanMemberMapper.isCreator(memberId));
	}

	private void validateIsMember(Long memberId) {
		if (!isMember(memberId)) {
			throw new NotFoundException(String.format("회원 (%s) 는 챌린지 (%s) 에 참가 하고 있지 않습니다", memberId, id), MEMBER_IN_CHALLENGE);
		}
	}

	private boolean isMember(Long memberId) {
		return this.challengePlanMemberMapperList.stream()
				.anyMatch(challengePlanMemberMapper -> challengePlanMemberMapper.isMember(memberId));
	}

	public Challenge startChallengeByForce(Long memberId) {
		validateCreator(memberId);
		return startChallenge();
	}

	public Challenge startChallenge() {
		inactiveChallengePlan();
		final LocalDateTime now = LocalDateTime.now();
		Challenge challenge = Challenge.newInstance(name, description, color, now, now.plusDays(period));
		challenge.addMembers(this.challengePlanMemberMapperList.stream()
				.map(challengePlanMemberMapper -> challengePlanMemberMapper.convertChallengeMemberMapper(challenge))
				.collect(Collectors.toList()));
		return challenge;
	}

	private void inactiveChallengePlan() {
		this.isActive = false;
	}

	String getInvitationKey() {
		return this.invitationKey.getInvitationKey();
	}

	public String issueInvitationKey(Long memberId) {
		validateIsMember(memberId);
		return getInvitationKey();
	}

	public void refreshInvitationKey(Long memberId) {
		if (!isCreator(memberId)) {
			throw new NotAllowedException(String.format("멤버 (%s) 는 챌린지 (%s) 의 생성자가 아닙니다", memberId, this.id), MEMBER_IN_CHALLENGE);
		}
		this.invitationKey = InvitationKey.newInstance();
	}

	public List<Long> getMemberIds() {
		return this.challengePlanMemberMapperList.stream()
				.map(ChallengePlanMemberMapper::getMemberId)
				.collect(Collectors.toList());
	}

	public boolean isFullMember() {
		return this.maxMembersCount == this.currentMembersCount;
	}

}
