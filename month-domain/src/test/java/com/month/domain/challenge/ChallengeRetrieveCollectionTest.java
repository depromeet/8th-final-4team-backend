package com.month.domain.challenge;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class ChallengeRetrieveCollectionTest {

	@MethodSource("sources_todo_challenge")
	@ParameterizedTest
	void 아직_시작하지_않은_챌린지_분류(String name, LocalDateTime startDateTime, LocalDateTime endDateTime) {
		// given
		Challenge challenge = ChallengeCreator.create(name, startDateTime, endDateTime);

		ChallengeRetrieveCollection collection = ChallengeRetrieveCollection.of(Collections.singletonList(challenge));

		// when
		List<Challenge> challengeList = collection.getTodoChallengs();

		// then
		assertThat(challengeList).hasSize(1);
	}

	private static Stream<Arguments> sources_todo_challenge() {
		return Stream.of(
				Arguments.of("아직 시작하지 않은 챌린지1", LocalDateTime.of(2030, 1, 1, 0, 0), LocalDateTime.of(2030, 2, 1, 0, 0)),
				Arguments.of("아직 시작하지 않은 챌린지2", LocalDateTime.of(2030, 2, 1, 0, 0), LocalDateTime.of(2030, 3, 1, 0, 0))
		);
	}

	@MethodSource("sources_doing_challenge")
	@ParameterizedTest
	void 진행중인_않은_챌린지_분류(String name, LocalDateTime startDateTime, LocalDateTime endDateTime) {
		// given
		Challenge challenge = ChallengeCreator.create(name, startDateTime, endDateTime);

		ChallengeRetrieveCollection collection = ChallengeRetrieveCollection.of(Collections.singletonList(challenge));

		// when
		List<Challenge> challengeList = collection.getDoingChallenges();

		// then
		assertThat(challengeList).hasSize(1);
	}

	private static Stream<Arguments> sources_doing_challenge() {
		return Stream.of(
				Arguments.of("진행중인 챌린지1", LocalDateTime.of(2020, 1, 1, 0, 0), LocalDateTime.of(2030, 2, 1, 0, 0)),
				Arguments.of("진행중인 챌린지2", LocalDateTime.of(2020, 2, 1, 0, 0), LocalDateTime.of(2030, 3, 1, 0, 0))
		);
	}

	@MethodSource("sources_done_challenge")
	@ParameterizedTest
	void 이미_끝난_챌린지_분류(String name, LocalDateTime startDateTime, LocalDateTime endDateTime) {
		// given
		Challenge challenge = ChallengeCreator.create(name, startDateTime, endDateTime);

		ChallengeRetrieveCollection collection = ChallengeRetrieveCollection.of(Collections.singletonList(challenge));

		// when
		List<Challenge> challengeList = collection.getDoneChallenges();

		// then
		assertThat(challengeList).hasSize(1);
	}

	private static Stream<Arguments> sources_done_challenge() {
		return Stream.of(
				Arguments.of("끝난 챌린지1", LocalDateTime.of(2020, 1, 1, 0, 0), LocalDateTime.of(2020, 2, 1, 0, 0)),
				Arguments.of("끝난 챌린지2", LocalDateTime.of(2020, 2, 1, 0, 0), LocalDateTime.of(2020, 3, 1, 0, 0))
		);
	}

}
