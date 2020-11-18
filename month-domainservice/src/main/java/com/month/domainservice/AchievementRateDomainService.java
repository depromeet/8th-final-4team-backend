package com.month.domainservice;

import com.month.domain.accreditation.Accreditation;
import com.month.domain.accreditation.repository.AccreditationRepository;
import com.month.domain.challenge.Challenge;
import com.month.domain.challenge.ChallengeCollection;
import com.month.domain.challenge.ChallengeRepository;
import com.month.domainservice.dto.response.MemberAchieveRateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AchievementRateDomainService {

	private final ChallengeRepository challengeRepository;
	private final AccreditationRepository accreditationRepository;

	@Transactional(readOnly = true)
	public MemberAchieveRateResponse getMemberAchievementRate(Long memberId) {
		ChallengeCollection collection = ChallengeCollection.of(challengeRepository.findChallengesByMemberId(memberId));
		List<Accreditation> accreditationList = accreditationRepository.findAllByChallengeUuidList(collection.getChallengesUuidList());
		return MemberAchieveRateResponse.of(collection.getChallengesCount(), collection.getAchieveRateOfChallenge(accreditationList.size()));
	}

	@Transactional(readOnly = true)
	public int getChallengesCountWithFriend(Long memberId, Long friendId) {
		List<Challenge> challenges = challengeRepository.findNoFetchChallengesByMemberId(memberId);
		List<Challenge> challengesWithFriend = challenges.stream()
				.filter(challenge -> challenge.isApprovedMemberInChallenge(friendId))
				.collect(Collectors.toList());
		return challengesWithFriend.size();
	}

}
