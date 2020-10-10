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
public class ChallengeMemberMapper extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "challenge_id")
	private Challenge challenge;

	private Long memberId;

	@Enumerated(EnumType.STRING)
	private ChallengeRole role;

	ChallengeMemberMapper(Challenge challenge, Long memberId, ChallengeRole role) {
		this.challenge = challenge;
		this.memberId = memberId;
		this.role = role;
	}

	static ChallengeMemberMapper creator(Challenge challenge, Long memberId) {
		return new ChallengeMemberMapper(challenge, memberId, ChallengeRole.CREATOR);
	}

	static ChallengeMemberMapper participator(Challenge challenge, Long memberId) {
		return new ChallengeMemberMapper(challenge, memberId, ChallengeRole.PARTICIPATOR);
	}

}
