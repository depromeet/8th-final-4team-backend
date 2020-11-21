package com.month.service.challenge;

import com.month.domain.challenge.*;
import com.month.exception.ValidationException;
import com.month.service.MemberSetupTest;
import com.month.service.challenge.dto.request.CreateNewChallengeRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class ChallengeCreateServiceTest extends MemberSetupTest {

	@Autowired
	private ChallengeService challengeService;

	@Autowired
	private ChallengeRepository challengeRepository;

	@Autowired
	private ChallengeMemberMapperRepository challengeMemberMapperRepository;

	@AfterEach
	void cleanUp() {
		super.cleanup();
		challengeMemberMapperRepository.deleteAllInBatch();
		challengeRepository.deleteAllInBatch();
	}

	@Test
	void 새로운_챌린지를_생성한다() {
		// given
		String name = "운동하기";
		ChallengeType type = ChallengeType.EXERCISE;
		String color = "#000000";
		LocalDate startDate = LocalDate.of(2020, 1, 1);
		LocalDate endDate = LocalDate.of(2030, 1, 1);

		CreateNewChallengeRequest request = CreateNewChallengeRequest.testBuilder()
				.name(name)
				.type(type)
				.color(color)
				.startDate(startDate)
				.endDate(endDate)
				.friendIds(Collections.emptyList())
				.build();

		// when
		challengeService.createNewChallenge(request, memberId);

		// then
		List<Challenge> challenges = challengeRepository.findAll();
		assertThat(challenges).hasSize(1);

		Challenge challenge = challenges.get(0);
		assertThat(challenge.getName()).isEqualTo(name);
		assertThat(challenge.getColor()).isEqualTo(color);
		assertThat(challenge.getStartDateTime()).isEqualTo(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
		assertThat(challenge.getEndDateTime()).isEqualTo(LocalDateTime.of(2030, 1, 1, 23, 59, 59));
	}

	@Test
	void 챌린지_생성시_초대한_친구들은_PENDING_상태가된다() {
		// given
		CreateNewChallengeRequest request = CreateNewChallengeRequest.testBuilder()
				.name("운동하기")
				.type(ChallengeType.EXERCISE)
				.color("#000000")
				.startDate(LocalDate.of(2020, 1, 1))
				.endDate(LocalDate.of(2030, 1, 1))
				.friendIds(Collections.singletonList(10L))
				.build();

		// when
		challengeService.createNewChallenge(request, memberId);

		// then
		List<Challenge> challenges = challengeRepository.findAll();
		assertThat(challenges).hasSize(1);
		assertThat(challenges.get(0).getMembersCount()).isEqualTo(1);

		List<ChallengeMemberMapper> challengeMemberMappers = challengeMemberMapperRepository.findAll();
		assertThat(challengeMemberMappers).hasSize(2);
		ChallengeServiceTestUtils.assertThatChallengeMember(challengeMemberMappers.get(0), memberId, ChallengeRole.CREATOR, ChallengeMemberStatus.APPROVED);
		ChallengeServiceTestUtils.assertThatChallengeMember(challengeMemberMappers.get(1), 10L, ChallengeRole.PARTICIPATOR, ChallengeMemberStatus.PENDING);
	}


	@Test
	void 새로운_챌린지를_생성할때_초대할_친구_리스트에_자기자신이_포함될_수_없다() {
		// given
		CreateNewChallengeRequest request = CreateNewChallengeRequest.testBuilder()
				.name("운동하기")
				.type(ChallengeType.EXERCISE)
				.color("#000000")
				.startDate(LocalDate.of(2020, 1, 1))
				.endDate(LocalDate.of(2030, 1, 1))
				.friendIds(Collections.singletonList(memberId))
				.build();

		// when & then
		assertThatThrownBy(() -> {
			challengeService.createNewChallenge(request, memberId);
		}).isInstanceOf(ValidationException.class);
	}

	@Test
	void 시작_날짜를_종료_날짜_이후로_설정할_수없다() {
		// given
		LocalDate startDate = LocalDate.of(2020, 1, 2);
		LocalDate endDate = LocalDate.of(2020, 1, 1);

		CreateNewChallengeRequest request = CreateNewChallengeRequest.testBuilder()
				.name("운동하기")
				.type(ChallengeType.EXERCISE)
				.color("#000000")
				.startDate(startDate)
				.endDate(endDate)
				.friendIds(Collections.emptyList())
				.build();

		// when & then
		assertThatThrownBy(() -> {
			challengeService.createNewChallenge(request, memberId);
		}).isInstanceOf(ValidationException.class);
	}

}
