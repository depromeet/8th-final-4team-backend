package com.month.service.challenge;

import com.month.domain.challenge.Challenge;
import com.month.domain.challenge.ChallengeRepository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class ChallengeServiceUtils {

	static Challenge findChallengeByUuid(ChallengeRepository challengeRepository, String uuid) {
		Challenge challenge = challengeRepository.findChallengeByUuid(uuid);
		if (challenge == null) {
			throw new IllegalArgumentException(String.format("해당하는 UUid (%s) 를 가진 챌린지는 없습니다", uuid));
		}
		return challenge;
	}

	static Challenge findChallengeByInvitationKey(ChallengeRepository challengeRepository, String invitationKey) {
		Challenge challenge = challengeRepository.findChallengeByInvitationKey(invitationKey);
		if (challenge == null) {
			throw new IllegalArgumentException(String.format("해당하는 초대장 (%s) 를 가진 챌린지는 없습니다", invitationKey));
		}
		return challenge;
	}

}
