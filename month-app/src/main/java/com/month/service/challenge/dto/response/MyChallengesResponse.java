package com.month.service.challenge.dto.response;

import com.month.domain.challenge.Challenge;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class MyChallengesResponse {

	private final List<ChallengeResponse> doneChallenges = new ArrayList<>();

	private final List<ChallengeResponse> doingChallenges = new ArrayList<>();

	private final List<ChallengeResponse> todoChallenges = new ArrayList<>();

	private MyChallengesResponse(List<ChallengeResponse> doneChallenges, List<ChallengeResponse> doingChallenges, List<ChallengeResponse> todoChallenges) {
		this.doneChallenges.addAll(doneChallenges);
		this.doingChallenges.addAll(doingChallenges);
		this.todoChallenges.addAll(todoChallenges);
	}

	public static MyChallengesResponse of(List<Challenge> doneChallenges, List<Challenge> doingChallenges, List<Challenge> todoChallenges) {
		List<ChallengeResponse> doneChallengesResponses = doneChallenges.stream()
				.map(ChallengeResponse::of)
				.collect(Collectors.toList());
		List<ChallengeResponse> doingChallengesResponses = doingChallenges.stream()
				.map(ChallengeResponse::of)
				.collect(Collectors.toList());
		List<ChallengeResponse> todoChallengesResponses = todoChallenges.stream()
				.map(ChallengeResponse::of)
				.collect(Collectors.toList());
		return new MyChallengesResponse(doneChallengesResponses, doingChallengesResponses, todoChallengesResponses);
	}

}
