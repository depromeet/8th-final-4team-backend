package com.month.service.challenge;

import com.month.domain.challenge.Challenge;
import com.month.domain.challenge.ChallengeCreator;
import com.month.domain.challenge.ChallengeRepository;
import com.month.domain.challenge.ChallengeType;
import com.month.service.MemberSetupTest;
import com.month.service.challenge.dto.request.CreateNewChallengeRequest;
import com.month.service.challenge.dto.request.GetChallengeInfoByInvitationKeyRequest;
import com.month.service.challenge.dto.request.GetInvitationKeyRequest;
import com.month.service.challenge.dto.response.ChallengeResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

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

	@Test
	void 친구를_초대하기_위해_챌린지의_초대키를_발급받는다() {
		// given
		Challenge challenge = ChallengeCreator.create("챌린지",
				LocalDateTime.of(2030, 1, 1, 0, 0),
				LocalDateTime.of(2030, 1, 7, 0, 0));
		challenge.addCreator(memberId);
		challengeRepository.save(challenge);

		GetInvitationKeyRequest request = GetInvitationKeyRequest.testInstance(challenge.getUuid());

		// when
		String invitationKey = challengeService.getInvitationKey(request, memberId);

		// then
		assertThat(invitationKey).isNotEmpty();
	}

	@Test
	void 챌린지에_참가하고_있지않으면_초대키를_발급_받을_수없다() {
		// given
		Challenge challenge = ChallengeCreator.create("챌린지",
				LocalDateTime.of(2030, 1, 1, 0, 0),
				LocalDateTime.of(2030, 1, 7, 0, 0));
		challengeRepository.save(challenge);

		GetInvitationKeyRequest request = GetInvitationKeyRequest.testInstance(challenge.getUuid());

		// when & then
		assertThatThrownBy(() -> {
			challengeService.getInvitationKey(request, memberId);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void 초대키를_통해_챌린지의_정보를_받아온다() {
		// given
		String name = "운동하기";
		ChallengeType type = ChallengeType.EXERCISE;
		String color = "#000000";
		LocalDateTime startDateTime = LocalDateTime.of(2030, 1, 1, 0, 0);
		LocalDateTime endDateTime = LocalDateTime.of(2030, 1, 7, 0, 0);

		Challenge challenge = ChallengeCreator.create(name, type, color, startDateTime, endDateTime);
		challenge.addCreator(memberId);
		challengeRepository.save(challenge);

		String invitationKey = challenge.issueInvitationKey(memberId);

		GetChallengeInfoByInvitationKeyRequest request = GetChallengeInfoByInvitationKeyRequest.testInstance(invitationKey);

		// when
		ChallengeResponse response = challengeService.getChallengeInfoByInvitationKey(request);

		// then
		assertChallengeResponse(response, name, type, color, startDateTime, endDateTime, 1);
	}

	private void assertChallengeResponse(ChallengeResponse response, String name, ChallengeType type, String color, LocalDateTime startDateTime, LocalDateTime endDateTime, int membersCount) {
		assertThat(response.getName()).isEqualTo(name);
		assertThat(response.getType()).isEqualTo(type);
		assertThat(response.getColor()).isEqualTo(color);
		assertThat(response.getStartDateTime()).isEqualTo(startDateTime);
		assertThat(response.getEndDateTime()).isEqualTo(endDateTime);
		assertThat(response.getMembersCount()).isEqualTo(membersCount);
	}

}