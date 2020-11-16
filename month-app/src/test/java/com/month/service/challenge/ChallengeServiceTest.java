package com.month.service.challenge;

import com.month.domain.challenge.Challenge;
import com.month.domain.challenge.ChallengeCreator;
import com.month.domain.challenge.ChallengeMemberMapper;
import com.month.domain.challenge.ChallengeMemberMapperRepository;
import com.month.domain.challenge.ChallengePlan;
import com.month.domain.challenge.ChallengePlanCreator;
import com.month.domain.challenge.ChallengePlanMemberMapperRepository;
import com.month.domain.challenge.ChallengePlanRepository;
import com.month.domain.challenge.ChallengeRepository;
import com.month.domain.challenge.ChallengeRole;
import com.month.exception.NotAllowedException;
import com.month.service.MemberSetupTest;
import com.month.service.challenge.dto.request.StartChallengeRequest;
import com.month.service.challenge.dto.response.ChallengeInfoResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class ChallengeServiceTest extends MemberSetupTest {

	@Autowired
	private ChallengePlanRepository challengePlanRepository;

	@Autowired
	private ChallengePlanMemberMapperRepository challengePlanMemberMapperRepository;

	@Autowired
	private ChallengeRepository challengeRepository;

	@Autowired
	private ChallengeMemberMapperRepository challengeMemberMapperRepository;

	@Autowired
	private ChallengeService challengeService;

	@AfterEach
	void cleanUp() {
		super.cleanup();
		challengePlanMemberMapperRepository.deleteAllInBatch();
		challengePlanRepository.deleteAllInBatch();
		challengeMemberMapperRepository.deleteAllInBatch();
		challengeRepository.deleteAllInBatch();
	}

	@Test
	void 모든_멤버가_초대를_받아_자동으로_챌린지가_시작되면_챌린지_계획은_비활성화되고_챌린지가_시작된() {
		// given
		String name = "챌린지 이름";
		String description = "챌린지 설명";
		ChallengePlan challengePlan = ChallengePlanCreator.create(name, description, 30, 4);
		challengePlan.addCreator(memberId);
		challengePlanRepository.save(challengePlan);

		// when
		challengeService.autoStartChallenge(challengePlan.getId());

		// then
		List<ChallengePlan> challengePlans = challengePlanRepository.findAll();
		assertThat(challengePlans).hasSize(1);
		assertThat(challengePlans.get(0).isActive()).isFalse();

		List<Challenge> challenges = challengeRepository.findAll();
		assertThat(challenges).hasSize(1);
		assertChallenge(challenges.get(0), name, description);
	}

	@Test
	void 챌린지를_강제_시작하면_챌린지_계획은_비활성화되고_챌린지가_시작된다() {
		// given
		String name = "챌린지 이름";
		String description = "챌린지 설명";
		ChallengePlan challengePlan = ChallengePlanCreator.create(name, description, 30, 4);
		challengePlan.addCreator(memberId);
		challengePlanRepository.save(challengePlan);

		StartChallengeRequest request = StartChallengeRequest.testInstance(challengePlan.getId());

		// when
		challengeService.startChallenge(request, memberId);

		// then
		List<ChallengePlan> challengePlans = challengePlanRepository.findAll();
		assertThat(challengePlans).hasSize(1);
		assertThat(challengePlans.get(0).isActive()).isFalse();

		List<Challenge> challenges = challengeRepository.findAll();
		assertThat(challenges).hasSize(1);
		assertChallenge(challenges.get(0), name, description);
	}

	private void assertChallenge(Challenge challenge, String name, String description) {
		assertThat(challenge.getName()).isEqualTo(name);
		assertThat(challenge.getDescription()).isEqualTo(description);
	}

	@MethodSource("source_start_challenge_period_test")
	@ParameterizedTest
	void 챌린지를_시작하면_현재시간부터_Period_기간동안_설정된다(String name, int period) {
		// given
		ChallengePlan challengePlan = ChallengePlanCreator.create(name, period);
		challengePlan.addCreator(memberId);
		challengePlanRepository.save(challengePlan);

		StartChallengeRequest request = StartChallengeRequest.testInstance(challengePlan.getId());

		// when
		challengeService.startChallenge(request, memberId);

		// then
		List<Challenge> challenges = challengeRepository.findAll();
		final LocalDateTime now = LocalDateTime.now(); // TODO 개선 필요. (모킹해야함)
		assertThat(challenges.get(0).getStartDateTime()).isBetween(now.minusMinutes(1), now.plusMinutes(1));
		assertThat(challenges.get(0).getEndDateTime().minusDays(period)).isEqualTo(challenges.get(0).getStartDateTime());
	}

	private static Stream<Arguments> source_start_challenge_period_test() {
		return Stream.of(
				Arguments.of("한달 챌린지", 50),
				Arguments.of("한달 챌린지", 30),
				Arguments.of("2주 챌린지", 14),
				Arguments.of("1주 챌린지", 7)
		);
	}

	@Test
	void 챌린지를_시작하면_기존의_챌린지_계획에_있던_멤버들이_모두_복사된다() {
		// given
		ChallengePlan challengePlan = ChallengePlanCreator.create("챌린지 이름", "챌린지 설명", 30, 4);
		challengePlan.addCreator(memberId);
		challengePlanRepository.save(challengePlan);

		StartChallengeRequest request = StartChallengeRequest.testInstance(challengePlan.getId());

		// when
		challengeService.startChallenge(request, memberId);

		// then
		List<ChallengeMemberMapper> challengeMemberMappers = challengeMemberMapperRepository.findAll();
		assertThat(challengeMemberMappers).hasSize(1);
		assertChallengeMemberMapper(challengeMemberMappers.get(0), memberId, ChallengeRole.CREATOR);
	}

	private void assertChallengeMemberMapper(ChallengeMemberMapper challengeMemberMapper, Long memberId, ChallengeRole role) {
		assertThat(challengeMemberMapper.getMemberId()).isEqualTo(memberId);
		assertThat(challengeMemberMapper.getRole()).isEqualTo(role);
	}

	@Test
	void 챌린지를_강제_시작할때_생성자가_아니면_시작하지_못한다() {
		// given
		ChallengePlan challengePlan = ChallengePlanCreator.create("챌린지 이름", "챌린지 설명", 30, 4);
		challengePlan.addParticipator(memberId);
		challengePlanRepository.save(challengePlan);

		StartChallengeRequest request = StartChallengeRequest.testInstance(challengePlan.getId());

		// when & then
		assertThatThrownBy(() -> {
			challengeService.startChallenge(request, memberId);
		}).isInstanceOf(NotAllowedException.class);
	}

	@Test
	void 나의_진행중인_챌린지_리스트를_불러온다() {
		// given
		Challenge challenge1 = ChallengeCreator.create("챌린지1", LocalDateTime.of(2020, 5, 10, 0, 0), LocalDateTime.of(2030, 11, 9, 0, 0));
		challenge1.addCreator(memberId);
		Challenge challenge2 = ChallengeCreator.create("챌린지2", LocalDateTime.of(2020, 6, 10, 0, 0), LocalDateTime.of(2030, 12, 9, 0, 0));
		challenge2.addParticipator(memberId);
		challengeRepository.saveAll(Arrays.asList(challenge1, challenge2));

		// when
		List<ChallengeInfoResponse> challengeInfoResponses = challengeService.retrieveMyChallengesList(memberId);

		// then
		assertThat(challengeInfoResponses).hasSize(2);
		assertThat(challengeInfoResponses).extracting("uuid").containsExactly(challenge1.getUuid(), challenge2.getUuid());
	}

	@ParameterizedTest
	@MethodSource("source_load_active_challenges_ongoing")
	void 현재_진행중인_챌린지를_불러오는_기능_진행중인_경우_진행중인_챌린지에_포함된다(String name, LocalDateTime startDateTime, LocalDateTime endDateTime) {
		// given
		Challenge challenge = ChallengeCreator.create(name, startDateTime, endDateTime);
		challenge.addCreator(memberId);
		challengeRepository.save(challenge);

		// when
		List<ChallengeInfoResponse> challengeInfoResponses = challengeService.retrieveMyChallengesList(memberId);

		// then
		assertThat(challengeInfoResponses).hasSize(1);
		assertThat(challengeInfoResponses.get(0).getUuid()).isEqualTo(challenge.getUuid());
	}

	private static Stream<Arguments> source_load_active_challenges_ongoing() {
		return Stream.of(
				Arguments.of("챌린지1", LocalDateTime.of(2020, 6, 10, 0, 0), LocalDateTime.of(2030, 6, 30, 0, 0)),
				Arguments.of("챌린지2", LocalDateTime.of(2020, 7, 10, 0, 0), LocalDateTime.of(2030, 7, 20, 0, 0))
		);
	}

	@ParameterizedTest
	@MethodSource("source_load_active_challenges_finish")
	void 현재_진행중인_챌린지를_불러오는_기능_이미_끝난경우_진행중인_챌린지에_포함되지_않는다(String name, LocalDateTime startDateTime, LocalDateTime endDateTime) {
		// given
		Challenge challenge = ChallengeCreator.create(name, startDateTime, endDateTime);
		challenge.addCreator(memberId);
		challengeRepository.save(challenge);

		// when
		List<ChallengeInfoResponse> challengeInfoResponses = challengeService.retrieveMyChallengesList(memberId);

		// then
		assertThat(challengeInfoResponses).isEmpty();
	}

	private static Stream<Arguments> source_load_active_challenges_finish() {
		return Stream.of(
				Arguments.of("챌린지1", LocalDateTime.of(2020, 6, 10, 0, 0), LocalDateTime.of(2020, 6, 30, 0, 0)),
				Arguments.of("챌린지2", LocalDateTime.of(2020, 7, 10, 0, 0), LocalDateTime.of(2020, 7, 20, 0, 0))
		);
	}

	@ParameterizedTest
	@MethodSource("source_load_active_challenges")
	void 현재_진행중인_챌린지를_불러오는_기능_아직_시작하지_않은_경우_진행중인_챌린지에_포함되지_않는다(String name, LocalDateTime startDateTime, LocalDateTime endDateTime) {
		// given
		Challenge challenge = ChallengeCreator.create(name, startDateTime, endDateTime);
		challenge.addCreator(memberId);
		challengeRepository.save(challenge);

		// when
		List<ChallengeInfoResponse> challengeInfoResponses = challengeService.retrieveMyChallengesList(memberId);

		// then
		assertThat(challengeInfoResponses).isEmpty();
	}

	private static Stream<Arguments> source_load_active_challenges() {
		return Stream.of(
				Arguments.of("챌린지1", LocalDateTime.of(2030, 6, 10, 0, 0), LocalDateTime.of(2030, 6, 30, 0, 0)),
				Arguments.of("챌린지2", LocalDateTime.of(2030, 7, 10, 0, 0), LocalDateTime.of(2030, 7, 20, 0, 0))
		);
	}

}