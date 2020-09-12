package com.month.domain.challenge;

import com.month.domain.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

	@Column(nullable = false)
	private Long memberId;

	@Enumerated(EnumType.STRING)
	private ChallengeRole role;

	private ChallengeMemberMapper(Challenge challenge, Long memberId, ChallengeRole role) {
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

	boolean isCreator(Long memberId) {
		return this.memberId.equals(memberId) && this.role.equals(ChallengeRole.CREATOR);
	}

	boolean isParticipator(Long memberId) {
		return this.memberId.equals(memberId) && this.role.equals(ChallengeRole.PARTICIPATOR);
	}

}
