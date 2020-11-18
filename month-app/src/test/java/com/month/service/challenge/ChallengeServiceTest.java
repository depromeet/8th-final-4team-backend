package com.month.service.challenge;

import com.month.domain.challenge.Challenge;
import com.month.domain.challenge.ChallengeRepository;
import com.month.domain.challenge.ChallengeType;
import com.month.service.MemberSetupTest;
import com.month.service.challenge.dto.request.CreateNewChallengeRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ChallengeServiceTest extends MemberSetupTest {

	@Autowired
	private ChallengeService challengeService;

	@Autowired
	private ChallengeRepository challengeRepository;

	@AfterEach
	void cleanUp() {
		challengeRepository.deleteAll();
	}

	@Test
	void 새로운_챌린지를_생성한다() {
		// given
		String name = "운동하기";

		ChallengeType type = ChallengeType.EXERCISE;

		String color = "#000000";

		LocalDate startDate = LocalDate.of(2020, 11, 1);

		LocalDate endDate = LocalDate.of(2020, 12, 1);

		CreateNewChallengeRequest request = CreateNewChallengeRequest.testBuilder()
				.name(name)
				.type(type)
				.color(color)
				.startDate(startDate)
				.endDate(endDate)
				.build();

		// when
		challengeService.createNewChallenge(request, memberId);

		// then
		List<Challenge> challenges = challengeRepository.findAll();
		assertThat(challenges).hasSize(1);

		Challenge challenge = challenges.get(0);
		assertThat(challenge.getName()).isEqualTo(name);
		assertThat(challenge.getColor()).isEqualTo(color);
		assertThat(challenge.getStartDateTime()).isEqualTo(LocalDateTime.of(2020, 11, 1, 0, 0, 0));
		assertThat(challenge.getEndDateTime()).isEqualTo(LocalDateTime.of(2020, 12, 1, 11, 59, 59));
	}

}