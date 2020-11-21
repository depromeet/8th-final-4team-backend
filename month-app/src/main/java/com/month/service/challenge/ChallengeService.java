package com.month.service.challenge;

import com.month.domain.challenge.Challenge;
import com.month.domain.challenge.ChallengeRepository;
import com.month.domain.challenge.ChallengeRetrieveCollection;
import com.month.service.challenge.dto.request.*;
import com.month.service.challenge.dto.response.ChallengeResponse;
import com.month.service.challenge.dto.response.InvitedChallengeListResponse;
import com.month.service.challenge.dto.response.MyChallengesResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ChallengeService {

	private final ChallengeRepository challengeRepository;

	@Transactional
	public ChallengeResponse createNewChallenge(CreateNewChallengeRequest request, Long memberId) {
		Challenge challenge = request.toEntity(memberId);
		challenge.addCreator(memberId);
		challenge.addPendingParticipators(request.getFriendIds());
		return ChallengeResponse.of(challengeRepository.save(challenge));
	}

	@Transactional(readOnly = true)
	public MyChallengesResponse retrieveMyChallenges(Long memberId) {
		List<Challenge> challengeList = challengeRepository.findChallengesByMemberId(memberId);
		ChallengeRetrieveCollection collection = ChallengeRetrieveCollection.of(challengeList);
		return MyChallengesResponse.of(collection.getDoneChallenges(), collection.getDoingChallenges(), collection.getTodoChallenges());
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

	@Transactional(readOnly = true)
	public List<InvitedChallengeListResponse> retrieveInvitedChallengeList(Long memberId) {
		List<Challenge> challenges = challengeRepository.findPendingChallengeByMemberId(memberId);
		return challenges.stream()
				.map(challenge -> InvitedChallengeListResponse.of(challenge, memberId))
				.collect(Collectors.toList());
	}

	@Transactional
	public void participateByInvitationKey(ParticipateChallengeRequest request, Long memberId) {
		Challenge challenge = ChallengeServiceUtils.findChallengeByInvitationKey(challengeRepository, request.getInvitationKey());
		challenge.participate(memberId);
	}

	@Transactional
	public void rejectInvitation(RejectInviteChallengeRequest request, Long memberId) {
		Challenge challenge = ChallengeServiceUtils.findChallengeByInvitationKey(challengeRepository, request.getInvitationKey());
		challenge.rejectParticipate(memberId);
	}

}
