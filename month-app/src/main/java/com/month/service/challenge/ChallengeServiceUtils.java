package com.month.service.challenge;

import com.month.domain.challenge.Challenge;
import com.month.domain.challenge.ChallengeRepository;

final class ChallengeServiceUtils {

	static Challenge findChallengeByUuid(ChallengeRepository challengeRepository, String uuid) {
		Challenge challenge = challengeRepository.findChallengeByUuid(uuid);
		if (challenge == null) {
			throw new IllegalArgumentException(String.format("해당 uuid (%s) 를 가진 챌린지는 존재하지 않습니다.", uuid));
		}
		return challenge;
	}

	static Challenge findChallengeByInvitationKey(ChallengeRepository challengeRepository, String invitationKey) {
		Challenge challenge = challengeRepository.findChallengeByInvitationKey(invitationKey);
		if (challenge == null) {
			throw new IllegalArgumentException(String.format("해당 초대키 (%s) 를 가진 챌린지는 존재하지 않습니다.", invitationKey));
		}
		return challenge;
	}

}
