package com.month.service.challenge;

import com.month.domain.challenge.ChallengeMemberMapper;
import com.month.domain.challenge.ChallengeMemberStatus;
import com.month.domain.challenge.ChallengeRole;

import static org.assertj.core.api.Assertions.assertThat;

final class ChallengeServiceTestUtils {

	static void assertThatChallengeMember(ChallengeMemberMapper challengeMemberMapper, Long memberId, ChallengeRole role, ChallengeMemberStatus status) {
		assertThat(challengeMemberMapper.getMemberId()).isEqualTo(memberId);
		assertThat(challengeMemberMapper.getRole()).isEqualTo(role);
		assertThat(challengeMemberMapper.getStatus()).isEqualTo(status);
	}

}
