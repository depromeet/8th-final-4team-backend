package com.month.service.challenge;

import com.month.domain.challenge.Challenge;
import com.month.domain.challenge.ChallengeRepository;
import com.month.service.challenge.dto.request.ChallengeCreateRequest;
import com.month.service.challenge.dto.request.ChallengeRetrieveRequest;
import com.month.service.challenge.dto.response.ChallengeInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ChallengeService {

	private final ChallengeRepository challengeRepository;

	@Transactional
	public ChallengeInfoResponse createNewChallenge(ChallengeCreateRequest request, Long memberId) {
		Challenge challenge = request.toEntity();
		challenge.addCreator(memberId);
		return ChallengeInfoResponse.of(challengeRepository.save(challenge));
	}

	@Transactional(readOnly = true)
	public ChallengeInfoResponse getChallengeInfo(ChallengeRetrieveRequest request, Long memberId) {
		Challenge challenge = ChallengeServiceUtils.findChallengeByUuid(challengeRepository, request.getUuid());
		challenge.validateMemberInChallenge(memberId);
		return ChallengeInfoResponse.of(challenge);
	}

}
