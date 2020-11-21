package com.month.domain.challenge;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ChallengeCollection {

	private final List<Challenge> challengeList = new ArrayList<>();

	private ChallengeCollection(List<Challenge> challengeList) {
		this.challengeList.addAll(challengeList);
	}

	public static ChallengeCollection of(List<Challenge> challenges) {
		return new ChallengeCollection(challenges);
	}

	public List<String> getChallengesUuidList() {
		return challengeList.stream()
				.map(Challenge::getUuid)
				.collect(Collectors.toList());
	}

	public int getChallengesCount() {
		return challengeList.size();
	}

	public double getAchieveRateOfChallenge(int achieveCount) {
		int totalChallengeDays = getTotalChallengeDays();
		if (totalChallengeDays == 0) {
			return 0;
		}
		double percent = (double) (achieveCount) / totalChallengeDays * 100;
		return Math.round(percent);
	}

	private int getTotalChallengeDays() {
		return challengeList.stream()
				.mapToInt(Challenge::calculateProgressDays)
				.sum();
	}

}
