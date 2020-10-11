package com.month.service.challenge;

import com.month.domain.challenge.Challenge;
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
import com.month.service.challenge.dto.response.ChallengePlanInfoResponse;
import com.month.service.challenge.dto.response.ChallengePlanInvitationInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class ChallengePlanServiceTest extends MemberSetupTest {

	@Autowired
	private ChallengePlanRepository challengePlanRepository;

	@Autowired
	private ChallengePlanMemberMapperRepository challengePlanMemberMapperRepository;

	@Autowired
	private ChallengeRepository challengeRepository;

	@Autowired
	private ChallengeMemberMapperRepository challengeMemberMapperRepository;

	@Autowired
	private ChallengePlanService challengePlanService;

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
		challengePlanService.createChallengePlan(request, memberId);

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
	void 혼자하는_챌린지_계획을_생성하면_Challenge_Plan_Entity_가_비활성화되고_바로_새로운_챌린지가_생성된다() {
		// given
		String name = "챌린지 계획";
		String description = "챌린지 설명";
		int period = 7;
		int maxMembersCount = 1;

		CreateChallengePlanRequest request = CreateChallengePlanRequest.testBuilder()
				.name(name)
				.description(description)
				.period(period)
				.maxMembersCount(maxMembersCount)
				.build();

		// when
		challengePlanService.createChallengePlan(request, memberId);

		// then
		List<ChallengePlan> challengePlans = challengePlanRepository.findAll();
		assertThat(challengePlans).hasSize(1);
		assertThat(challengePlans.get(0).isActive()).isFalse();

		List<Challenge> challenges = challengeRepository.findAll();
		assertThat(challenges).hasSize(1);
		assertBetweenStartAndEndDateTime(challenges.get(0), period);
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
		challengePlanService.createChallengePlan(request, memberId);

		// then
		List<ChallengePlanMemberMapper> challengePlanMemberMappers = challengePlanMemberMapperRepository.findAll();
		assertThat(challengePlanMemberMappers).hasSize(1);
		assertChallengePlanMemberMapper(challengePlanMemberMappers.get(0), memberId, ChallengeRole.CREATOR);
	}

	@Test
	void 나의_챌린지_계획_리스트를_조회한다() {
		// given
		ChallengePlan challengePlan1 = ChallengePlanCreator.create("챌린지 계획1", "설명1", 30, 4);
		challengePlan1.addCreator(memberId);

		ChallengePlan challengePlan2 = ChallengePlanCreator.create("챌린지 계획2", "설명2", 7, 3);
		challengePlan2.addParticipator(memberId);

		challengePlanRepository.saveAll(Arrays.asList(challengePlan1, challengePlan2));

		// when
		List<ChallengePlanInfoResponse> challengePlanInfoResponses = challengePlanService.retrieveMyChallengePlans(memberId);

		// then
		assertThat(challengePlanInfoResponses).hasSize(2);
		assertThatChallengePlanInfoResponse(challengePlanInfoResponses.get(0), challengePlan1.getName(), challengePlan1.getDescription(), challengePlan1.getPeriod(), challengePlan1.getMaxMembersCount());
		assertThatChallengePlanInfoResponse(challengePlanInfoResponses.get(1), challengePlan2.getName(), challengePlan2.getDescription(), challengePlan2.getPeriod(), challengePlan2.getMaxMembersCount());
	}

	private void assertThatChallengePlanInfoResponse(ChallengePlanInfoResponse challengePlanInfoResponse, String name, String description, int period, int maxMembersCount) {
		assertThat(challengePlanInfoResponse.getName()).isEqualTo(name);
		assertThat(challengePlanInfoResponse.getDescription()).isEqualTo(description);
		assertThat(challengePlanInfoResponse.getPeriod()).isEqualTo(period);
		assertThat(challengePlanInfoResponse.getMaxMembersCount()).isEqualTo(maxMembersCount);
	}

	@Test
	void 챌린지의_초대키를_반환한다() {
		// given
		ChallengePlan challengePlan = ChallengePlanCreator.create("챌린지 이름", "챌린지 설명", 30, 4);
		challengePlan.addParticipator(memberId);
		challengePlanRepository.save(challengePlan);

		RetrieveChallengePlanInvitationKeyRequest request = new RetrieveChallengePlanInvitationKeyRequest(challengePlan.getId());

		// when
		String invitationKey = challengePlanService.getChallengePlanInvitationKey(request, memberId);

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
		challengePlanService.refreshChallengeInvitationKey(request, memberId);

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
			challengePlanService.refreshChallengeInvitationKey(request, memberId);
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
		ChallengePlanInvitationInfo response = challengePlanService.getChallengeInfoByInvitationKey(challengePlan.getInvitationKey());

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
			challengePlanService.getChallengeInfoByInvitationKey("example");
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void 초대키로_챌린지에_참가한다() {
		// given
		ChallengePlan challengePlan = ChallengePlanCreator.create("챌린지 이름", "챌린지 설명", 30, 4);
		challengePlan.addCreator(999L);
		challengePlanRepository.save(challengePlan);

		// when
		EnterChallengeByInvitationKeyRequest request = EnterChallengeByInvitationKeyRequest.testInstance(challengePlan.getInvitationKey());
		challengePlanService.enterChallengeByInvitationKey(request, memberId);

		// then
		List<ChallengePlanMemberMapper> challengePlanMemberMappers = challengePlanMemberMapperRepository.findAll();
		assertThat(challengePlanMemberMappers).hasSize(2);
		assertChallengePlanMemberMapper(challengePlanMemberMappers.get(0), 999L, ChallengeRole.CREATOR);
		assertChallengePlanMemberMapper(challengePlanMemberMappers.get(1), memberId, ChallengeRole.PARTICIPATOR);
	}

	@Test
	void 초대키로_챌린지에_참가로_모든_멤버가_입장하면_챌린지가_자동으로_시작된다() {
		// given
		ChallengePlan challengePlan = ChallengePlanCreator.create("챌린지 이름", "챌린지 설명", 15, 2);
		challengePlan.addCreator(999L);
		challengePlanRepository.save(challengePlan);

		// when
		EnterChallengeByInvitationKeyRequest request = EnterChallengeByInvitationKeyRequest.testInstance(challengePlan.getInvitationKey());
		challengePlanService.enterChallengeByInvitationKey(request, memberId);

		// then
		List<ChallengePlan> challengePlans = challengePlanRepository.findAll();
		assertThat(challengePlans).hasSize(1);
		assertThat(challengePlans.get(0).isActive()).isFalse();

		List<Challenge> challenges = challengeRepository.findAll();
		assertThat(challenges).hasSize(1);
		assertBetweenStartAndEndDateTime(challenges.get(0), challengePlan.getPeriod());
	}

	private void assertBetweenStartAndEndDateTime(Challenge challenge, int period) {
		assertThat(challenge.getEndDateTime().minusDays(period)).isEqualTo(challenge.getStartDateTime());
	}

	private void assertChallengePlanMemberMapper(ChallengePlanMemberMapper challengePlanMemberMapper, Long memberId, ChallengeRole role) {
		assertThat(challengePlanMemberMapper.getMemberId()).isEqualTo(memberId);
		assertThat(challengePlanMemberMapper.getRole()).isEqualTo(role);
	}

}
