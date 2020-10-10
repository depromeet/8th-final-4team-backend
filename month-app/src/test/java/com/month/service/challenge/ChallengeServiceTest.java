package com.month.service.challenge;

import com.month.domain.challenge.Challenge;
import com.month.domain.challenge.ChallengeCreator;
import com.month.domain.challenge.ChallengeMemberMapper;
import com.month.domain.challenge.ChallengeMemberMapperRepository;
import com.month.domain.challenge.ChallengePlan;
import com.month.domain.challenge.ChallengePlanCreator;
import com.month.domain.challenge.ChallengePlanMemberMapper;
import com.month.domain.challenge.ChallengePlanMemberMapperRepository;
import com.month.domain.challenge.ChallengePlanRepository;
import com.month.domain.challenge.ChallengeRepository;
import com.month.domain.challenge.ChallengeRole;
import com.month.service.MemberSetupTest;
import com.month.service.challenge.dto.request.CreateChallengePlanRequest;
import com.month.service.challenge.dto.request.EnterChallengeByInvitationKeyRequest;
import com.month.service.challenge.dto.request.RefreshChallengeInvitationKeyRequest;
import com.month.service.challenge.dto.request.RetrieveChallengePlanInvitationKeyRequest;
import com.month.service.challenge.dto.request.StartChallengeRequest;
import com.month.service.challenge.dto.response.ChallengeInfoResponse;
import com.month.service.challenge.dto.response.ChallengePlanInvitationInfo;
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
	void 챌린지_계획을_생성하면_Challenge_Plan_Entity_가_생성된다() {
		// given
		String name = "챌린지 계획";
		String description = "챌린지 설명";
		int period = 30;
		int maxMembersCount = 4;

		CreateChallengePlanRequest request = CreateChallengePlanRequest.testBuilder()
				.name(name)
				.description(description)
				.period(period)
				.maxMembersCount(maxMembersCount)
				.build();

		// when
		challengeService.createChallengePlan(request, memberId);

		// then
		List<ChallengePlan> challengePlans = challengePlanRepository.findAll();
		assertThat(challengePlans).hasSize(1);
		assertChallengePlan(challengePlans.get(0), name, description, period, maxMembersCount);
	}

	private void assertChallengePlan(ChallengePlan challengePlan, String name, String description, int period, int maxMembersCount) {
		assertThat(challengePlan.getName()).isEqualTo(name);
		assertThat(challengePlan.getDescription()).isEqualTo(description);
		assertThat(challengePlan.getPeriod()).isEqualTo(period);
		assertThat(challengePlan.getMaxMembersCount()).isEqualTo(maxMembersCount);
	}

	@Test
	void 챌린지_계획을_생성한_사람이_생성자가_된다() {
		// given
		CreateChallengePlanRequest request = CreateChallengePlanRequest.testBuilder()
				.name("챌린지 계획")
				.description("챌린지 설명")
				.period(30)
				.maxMembersCount(4)
				.build();

		// when
		challengeService.createChallengePlan(request, memberId);

		// then
		List<ChallengePlanMemberMapper> challengePlanMemberMappers = challengePlanMemberMapperRepository.findAll();
		assertThat(challengePlanMemberMappers).hasSize(1);
		assertChallengePlanMemberMapper(challengePlanMemberMappers.get(0), memberId, ChallengeRole.CREATOR);
	}

	@Test
	void 챌린지를_시작하면_Challenge_Entity_가_생성된다() {
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
		List<Challenge> challenges = challengeRepository.findAll();
		assertThat(challenges).hasSize(1);
		assertChallenge(challenges.get(0), name, description);
	}

	private void assertChallenge(Challenge challenge, String name, String description) {
		assertThat(challenge.getName()).isEqualTo(name);
		assertThat(challenge.getDescription()).isEqualTo(description);
	}

	@Test
	void 챌린지를_시작하면_현재시간부터_Period_기간동안_설정된다() {
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
		List<Challenge> challenges = challengeRepository.findAll();
		assertThat(challenges.get(0).getEndDateTime().minusDays(30)).isEqualTo(challenges.get(0).getStartDateTime());
	}

	@Test
	void 챌린지를_시작하면_기존의_챌린지_계획에_있던_멤버들이_복사된다() {
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
	void 챌린지를_시작할때_생성자가_아니면_시작하지_못한다() {
		// given
		ChallengePlan challengePlan = ChallengePlanCreator.create("챌린지 이름", "챌린지 설명", 30, 4);
		challengePlan.addParticipator(memberId);
		challengePlanRepository.save(challengePlan);

		StartChallengeRequest request = StartChallengeRequest.testInstance(challengePlan.getId());

		// when & then
		assertThatThrownBy(() -> {
			challengeService.startChallenge(request, memberId);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void 챌린지의_초대키를_반환한다() {
		// given
		ChallengePlan challengePlan = ChallengePlanCreator.create("챌린지 이름", "챌린지 설명", 30, 4);
		challengePlan.addParticipator(memberId);
		challengePlanRepository.save(challengePlan);

		RetrieveChallengePlanInvitationKeyRequest request = new RetrieveChallengePlanInvitationKeyRequest(challengePlan.getId());

		// when
		String invitationKey = challengeService.getChallengePlanInvitationKey(request, memberId);

		// then
		assertThat(invitationKey).isNotNull();
	}

	@Test
	void 챌린지의_초대키를_재발급한다() {
		// given
		ChallengePlan challengePlan = ChallengePlanCreator.create("챌린지 이름", "챌린지 설명", 30, 4);
		challengePlan.addCreator(memberId);
		challengePlanRepository.save(challengePlan);

		String beforeInvitationKey = challengePlan.getInvitationKey();

		RefreshChallengeInvitationKeyRequest request = RefreshChallengeInvitationKeyRequest.testInstance(challengePlan.getId());

		// when
		challengeService.refreshChallengeInvitationKey(request, memberId);

		// then
		List<ChallengePlan> challengePlans = challengePlanRepository.findAll();
		assertThat(challengePlans).hasSize(1);

		assertThat(challengePlans.get(0).getInvitationKey()).isNotEmpty();
		assertThat(challengePlans.get(0).getInvitationKey()).isNotEqualTo(beforeInvitationKey);
	}

	@Test
	void 생성자만이_챌린지의_초대키를_재발급할수_있다() {
		// given
		ChallengePlan challengePlan = ChallengePlanCreator.create("챌린지 이름", "챌린지 설명", 30, 4);
		challengePlan.addParticipator(memberId);
		challengePlanRepository.save(challengePlan);

		RefreshChallengeInvitationKeyRequest request = RefreshChallengeInvitationKeyRequest.testInstance(challengePlan.getId());

		// when & then
		assertThatThrownBy(() -> {
			challengeService.refreshChallengeInvitationKey(request, memberId);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void 초대키로_챌린지의_간단한_정보를_반환한다() {
		// given
		String name = "챌린지 계획";
		String description = "챌린지 설명";
		int period = 30;
		int maxMembersCount = 4;

		ChallengePlan challengePlan = ChallengePlanCreator.create(name, description, period, maxMembersCount);
		challengePlan.addParticipator(memberId);
		challengePlanRepository.save(challengePlan);

		// when
		ChallengePlanInvitationInfo response = challengeService.getChallengeInfoByInvitationKey(challengePlan.getInvitationKey());

		// then
		assertChallengePlanInvitationInfo(response, name, description, period, maxMembersCount);
	}

	private void assertChallengePlanInvitationInfo(ChallengePlanInvitationInfo response, String name, String description, int period, int maxMembersCount) {
		assertThat(response.getName()).isEqualTo(name);
		assertThat(response.getDescription()).isEqualTo(description);
		assertThat(response.getPeriod()).isEqualTo(period);
		assertThat(response.getMaxMembersCount()).isEqualTo(maxMembersCount);
	}

	@Test
	void 해당하는_초대키가_없을때_초대키_정보를_불러오면_에러발생한다() {
		// when & then
		assertThatThrownBy(() -> {
			challengeService.getChallengeInfoByInvitationKey("example");
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void 초대키로_챌린지에_참가한다() {
		// given
		ChallengePlan challengePlan = ChallengePlanCreator.create("챌린지 이름", "챌린지 설명", 30, 4);
		challengePlanRepository.save(challengePlan);

		// when
		EnterChallengeByInvitationKeyRequest request = EnterChallengeByInvitationKeyRequest.testInstance(challengePlan.getInvitationKey());
		challengeService.enterChallengeByInvitationKey(request, memberId);

		// then
		List<ChallengePlanMemberMapper> challengePlanMemberMappers = challengePlanMemberMapperRepository.findAll();
		assertThat(challengePlanMemberMappers).hasSize(1);
		assertChallengePlanMemberMapper(challengePlanMemberMappers.get(0), memberId, ChallengeRole.PARTICIPATOR);
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
		List<ChallengeInfoResponse> challengeInfoResponses = challengeService.retrieveMyChallengeList(memberId);

		// then
		assertThat(challengeInfoResponses).hasSize(2);
		assertThat(challengeInfoResponses).extracting("uuid").containsExactly(challenge1.getUuid(), challenge2.getUuid());
	}

	@ParameterizedTest
	@MethodSource("source_load_active_challenges_ongoing")
	void 현재_진행중인_챌린지를_불러오는_기능_진행중인_경우(String name, LocalDateTime startDateTime, LocalDateTime endDateTime) {
		// given
		Challenge challenge = ChallengeCreator.create(name, startDateTime, endDateTime);
		challenge.addCreator(memberId);
		challengeRepository.save(challenge);

		// when
		List<ChallengeInfoResponse> challengeInfoResponses = challengeService.retrieveMyChallengeList(memberId);

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
	void 현재_진행중인_챌린지를_불러오는_기능_이미_끝난경우(String name, LocalDateTime startDateTime, LocalDateTime endDateTime) {
		// given
		Challenge challenge = ChallengeCreator.create(name, startDateTime, endDateTime);
		challenge.addCreator(memberId);
		challengeRepository.save(challenge);

		// when
		List<ChallengeInfoResponse> challengeInfoResponses = challengeService.retrieveMyChallengeList(memberId);

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
	void 현재_진행중인_챌린지를_불러오는_기능_아직_시작하지_않은_경우(String name, LocalDateTime startDateTime, LocalDateTime endDateTime) {
		// given
		Challenge challenge = ChallengeCreator.create(name, startDateTime, endDateTime);
		challenge.addCreator(memberId);
		challengeRepository.save(challenge);

		// when
		List<ChallengeInfoResponse> challengeInfoResponses = challengeService.retrieveMyChallengeList(memberId);

		// then
		assertThat(challengeInfoResponses).isEmpty();
	}

	private static Stream<Arguments> source_load_active_challenges() {
		return Stream.of(
				Arguments.of("챌린지1", LocalDateTime.of(2030, 6, 10, 0, 0), LocalDateTime.of(2030, 6, 30, 0, 0)),
				Arguments.of("챌린지2", LocalDateTime.of(2030, 7, 10, 0, 0), LocalDateTime.of(2030, 7, 20, 0, 0))
		);
	}

	private void assertChallengePlanMemberMapper(ChallengePlanMemberMapper challengePlanMemberMapper, Long memberId, ChallengeRole role) {
		assertThat(challengePlanMemberMapper.getMemberId()).isEqualTo(memberId);
		assertThat(challengePlanMemberMapper.getRole()).isEqualTo(role);
	}

}