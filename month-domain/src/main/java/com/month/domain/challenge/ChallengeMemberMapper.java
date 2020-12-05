package com.month.domain.challenge;

import com.month.domain.BaseTimeEntity;
import com.month.exception.NotAllowedException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import static com.month.exception.type.ExceptionDescriptionType.MEMBER_IN_CHALLENGE;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ChallengeMemberMapper extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "challenge_id", nullable = false)
	private Challenge challenge;

	@Column(nullable = false)
	private Long memberId;

	@Enumerated(EnumType.STRING)
	private ChallengeRole role;

	@Enumerated(EnumType.STRING)
	private ChallengeMemberStatus status;

	private ChallengeMemberMapper(Challenge challenge, Long memberId, ChallengeRole role, ChallengeMemberStatus status) {
		this.challenge = challenge;
		this.memberId = memberId;
		this.role = role;
		this.status = status;
	}

	static ChallengeMemberMapper creator(Challenge challenge, Long memberId) {
		return new ChallengeMemberMapper(challenge, memberId, ChallengeRole.CREATOR, ChallengeMemberStatus.APPROVED);
	}

	static ChallengeMemberMapper participator(Challenge challenge, Long memberId, ChallengeMemberStatus status) {
		return new ChallengeMemberMapper(challenge, memberId, ChallengeRole.PARTICIPATOR, status);
	}

	boolean isParticipating(Long memberId) {
		return this.memberId.equals(memberId) && this.status.isParticipating();
	}

	boolean isMember(Long memberId) {
		return this.memberId.equals(memberId);
	}

	void approve() {
		validateInvitedMember(memberId);
		this.status = ChallengeMemberStatus.APPROVED;
	}

	void reject() {
		validateInvitedMember(memberId);
		this.status = ChallengeMemberStatus.REJECT;
	}

	private void validateInvitedMember(Long memberId) {
		if (!isInvitedMember(memberId)) {
			throw new NotAllowedException(String.format("멤버 (%s)는 초대받은 사용자가 아닙니다", memberId), MEMBER_IN_CHALLENGE);
		}
	}

	boolean isInvitedMember(Long memberId) {
		return this.memberId.equals(memberId) && this.status.equals(ChallengeMemberStatus.PENDING);
	}

}
