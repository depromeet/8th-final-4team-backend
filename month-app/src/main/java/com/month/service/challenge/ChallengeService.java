package com.month.service.challenge;

import com.month.domain.challenge.Challenge;
import com.month.domain.challenge.ChallengeRepository;
import com.month.domain.challenge.ChallengeRetrieveCollection;
import com.month.service.challenge.dto.request.CreateNewChallengeRequest;
import com.month.service.challenge.dto.request.GetChallengeInfoByInvitationKeyRequest;
import com.month.service.challenge.dto.request.GetInvitationKeyRequest;
import com.month.service.challenge.dto.response.ChallengeResponse;
import com.month.service.challenge.dto.response.MyChallengesResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ChallengeService {

	private final ChallengeRepository challengeRepository;

	@Transactional
	public ChallengeResponse createNewChallenge(CreateNewChallengeRequest request, Long memberId) {
		Challenge challenge = request.toEntity();
		challenge.addCreator(memberId);
		challenge.addPendingParticipators(request.getFriendIds());
		return ChallengeResponse.of(challengeRepository.save(challenge));
	}

	@Transactional(readOnly = true)
	public MyChallengesResponse retrieveMyChallenges(Long memberId) {
		List<Challenge> challengeList = challengeRepository.findChallengesByMemberId(memberId);
		ChallengeRetrieveCollection collection = ChallengeRetrieveCollection.of(challengeList);
		return MyChallengesResponse.of(collection.getDoneChallenges(), collection.getDoingChallenges(), collection.getTodoChallengs());
	}

	@Transactional(readOnly = true)
	public String getInvitationKey(GetInvitationKeyRequest request, Long memberId) {
		Challenge challenge = ChallengeServiceUtils.findChallengeByUuid(challengeRepository, request.getChallengeUuid());
		return challenge.issueInvitationKey(memberId);
	}

	@Transactional(readOnly = true)
	public ChallengeResponse getChallengeInfoByInvitationKey(GetChallengeInfoByInvitationKeyRequest request) {
		Challenge challenge = ChallengeServiceUtils.findChallengeByInvitationKey(challengeRepository, request.getInvitationKey());
		return ChallengeResponse.of(challenge);
	}

}
