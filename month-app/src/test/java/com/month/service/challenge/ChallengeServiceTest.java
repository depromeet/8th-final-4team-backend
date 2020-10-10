package com.month.service.challenge;

import com.month.domain.ChallengeMemberMapperRepository;
import com.month.domain.challenge.Challenge;
import com.month.domain.challenge.ChallengeMemberMapper;
import com.month.domain.challenge.ChallengePlan;
import com.month.domain.challenge.ChallengePlanCreator;
import com.month.domain.challenge.ChallengePlanMemberMapper;
import com.month.domain.challenge.ChallengePlanMemberMapperRepository;
import com.month.domain.challenge.ChallengePlanRepository;
import com.month.domain.challenge.ChallengeRepository;
import com.month.domain.challenge.ChallengeRole;
import com.month.service.MemberSetupTest;
import com.month.service.challenge.dto.request.CreateChallengePlanRequest;
import com.month.service.challenge.dto.request.StartChallengeRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

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

	private void assertChallenge(Challenge challenge, String name, String description) {
		assertThat(challenge.getName()).isEqualTo(name);
		assertThat(challenge.getDescription()).isEqualTo(description);
	}

	private void assertChallengePlanMemberMapper(ChallengePlanMemberMapper challengePlanMemberMapper, Long memberId, ChallengeRole role) {
		assertThat(challengePlanMemberMapper.getMemberId()).isEqualTo(memberId);
		assertThat(challengePlanMemberMapper.getRole()).isEqualTo(role);
	}

	private void assertChallengePlan(ChallengePlan challengePlan, String name, String description, int period, int maxMembersCount) {
		assertThat(challengePlan.getName()).isEqualTo(name);
		assertThat(challengePlan.getDescription()).isEqualTo(description);
		assertThat(challengePlan.getPeriod()).isEqualTo(period);
		assertThat(challengePlan.getMaxMembersCount()).isEqualTo(maxMembersCount);
	}

}