package com.month.domain.challenge;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ChallengeRetrieveCollection {

	private final List<Challenge> challengeList = new ArrayList<>();

	private ChallengeRetrieveCollection(List<Challenge> challengeList) {
		this.challengeList.addAll(challengeList);
	}

	public static ChallengeRetrieveCollection of(List<Challenge> challenges) {
		return new ChallengeRetrieveCollection(challenges);
	}

	public List<Challenge> getDoingChallenges() {
		return this.challengeList.stream()
				.filter(Challenge::isDoing)
				.collect(Collectors.toList());
	}

	public List<Challenge> getDoneChallenges() {
		return this.challengeList.stream()
				.filter(Challenge::isDone)
				.collect(Collectors.toList());
	}

	public List<Challenge> getTodoChallengs() {
		return this.challengeList.stream()
				.filter(Challenge::isTodo)
				.collect(Collectors.toList());
	}

}
