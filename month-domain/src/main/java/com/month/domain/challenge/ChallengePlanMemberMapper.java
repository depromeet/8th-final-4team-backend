package com.month.domain.challenge;

import com.month.domain.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ChallengePlanMemberMapper extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "challenge_plan_id")
	private ChallengePlan challengePlan;

	private Long memberId;

	@Enumerated(EnumType.STRING)
	private ChallengeRole role;

	private ChallengePlanMemberMapper(ChallengePlan challengePlan, Long memberId, ChallengeRole role) {
		this.challengePlan = challengePlan;
		this.memberId = memberId;
		this.role = role;
	}

	static ChallengePlanMemberMapper creator(ChallengePlan challengePlan, Long memberId) {
		return new ChallengePlanMemberMapper(challengePlan, memberId, ChallengeRole.CREATOR);
	}

	static ChallengePlanMemberMapper participator(ChallengePlan challengePlan, Long memberId) {
		return new ChallengePlanMemberMapper(challengePlan, memberId, ChallengeRole.PARTICIPATOR);
	}

	boolean isCreator(Long memberId) {
		return this.memberId.equals(memberId) && this.role.equals(ChallengeRole.CREATOR);
	}

	boolean isMember(Long memberId) {
		return this.memberId.equals(memberId);
	}

	ChallengeMemberMapper convertChallengeMemberMapper(Challenge challenge) {
		return new ChallengeMemberMapper(challenge, memberId, role);
	}

}
