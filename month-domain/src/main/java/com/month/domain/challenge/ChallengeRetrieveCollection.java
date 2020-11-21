package com.month.domain.challenge;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ChallengeRetrieveCollection {

	private final List<Challenge> challengeList = new ArrayList<>();
	private LocalDateTime now;

	private ChallengeRetrieveCollection(List<Challenge> challengeList) {
		this.challengeList.addAll(challengeList);
		now = LocalDateTime.now();
	}

	public static ChallengeRetrieveCollection of(List<Challenge> challenges) {
		return new ChallengeRetrieveCollection(challenges);
	}

	public List<Challenge> getDoingChallenges() {
		return this.challengeList.stream()
				.filter(challenge -> challenge.isDoing(now))
				.collect(Collectors.toList());
	}

	public List<Challenge> getDoneChallenges() {
		return this.challengeList.stream()
				.filter(challenge -> challenge.isDone(now))
				.collect(Collectors.toList());
	}

	public List<Challenge> getTodoChallenges() {
		return this.challengeList.stream()
				.filter(challenge -> challenge.isTodo(now))
				.collect(Collectors.toList());
	}

}
