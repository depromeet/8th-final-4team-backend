package com.month.service.challenge;

import com.month.domain.challenge.*;
import com.month.domain.member.Member;
import com.month.domain.member.MemberCreator;
import com.month.domain.member.MemberRepository;
import com.month.exception.NotAllowedException;
import com.month.exception.NotFoundException;
import com.month.exception.ValidationException;
import com.month.service.MemberSetupTest;
import com.month.service.challenge.dto.request.CreateNewChallengeRequest;
import com.month.service.challenge.dto.request.GetChallengeInfoByInvitationKeyRequest;
import com.month.service.challenge.dto.request.GetInvitationKeyRequest;
import com.month.service.challenge.dto.request.ParticipateChallengeRequest;
import com.month.service.challenge.dto.response.ChallengeResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class ChallengeServiceTest extends MemberSetupTest {

	@Autowired
	private ChallengeService challengeService;

	@Autowired
	private ChallengeRepository challengeRepository;

	@Autowired
	private ChallengeMemberMapperRepository challengeMemberMapperRepository;

	@Autowired
	private MemberRepository memberRepository;

	@AfterEach
	void cleanUp() {
		super.cleanup();
		challengeMemberMapperRepository.deleteAllInBatch();
		challengeRepository.deleteAllInBatch();
	}

	private Member friend;

	@BeforeEach
	void setUpFriend() {
		friend = memberRepository.save(MemberCreator.create("friend@gmail.com"));
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
		assertThatChallengeMember(challengeMemberMappers.get(0), memberId, ChallengeRole.CREATOR, ChallengeMemberStatus.APPROVED);
		assertThatChallengeMember(challengeMemberMappers.get(1), 10L, ChallengeRole.PARTICIPATOR, ChallengeMemberStatus.PENDING);
	}

	private void assertThatChallengeMember(ChallengeMemberMapper challengeMemberMapper, Long memberId, ChallengeRole role, ChallengeMemberStatus status) {
		assertThat(challengeMemberMapper.getMemberId()).isEqualTo(memberId);
		assertThat(challengeMemberMapper.getRole()).isEqualTo(role);
		assertThat(challengeMemberMapper.getStatus()).isEqualTo(status);
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
		}).isInstanceOf(NotFoundException.class);
	}

	@Test
	void 이미_시작한_챌린지에_대해_초대키를_발급받을_수없다() {
		// given
		LocalDateTime startDateTime = LocalDateTime.of(2020, 2, 1, 0, 1);
		LocalDateTime endDateTime = LocalDateTime.of(2030, 1, 1, 0, 0);
		Challenge challenge = ChallengeCreator.create("운동하기", ChallengeType.EXERCISE, "#000000", startDateTime, endDateTime);
		challenge.addCreator(memberId);
		challengeRepository.save(challenge);

		// when & then
		assertThatThrownBy(() -> {
			challenge.issueInvitationKey(memberId);
		}).isInstanceOf(NotAllowedException.class);
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

	@Test
	void 나의_초대받은_챌린지_리스트를_불러온다() {
		// given
		Challenge challenge = ChallengeCreator.create("운동하기", ChallengeType.EXERCISE, "#000000",
				LocalDateTime.of(2030, 1, 1, 0, 0),
				LocalDateTime.of(2030, 1, 7, 0, 0));
		challenge.addPendingParticipator(memberId);
		challengeRepository.save(challenge);

		// when
		List<ChallengeResponse> challengeResponses = challengeService.retrieveInvitedChallengeList(memberId);

		// then
		assertThat(challengeResponses).hasSize(1);
		assertThat(challengeResponses.get(0).getUuid()).isEqualTo(challenge.getUuid());
	}

	@Test
	void 나의_초대받은_챌린지_리스트를_불러올때_수락했거나_거절한경우는_조회되지_않는다() {
		// given
		Challenge challenge = ChallengeCreator.create("운동하기", ChallengeType.EXERCISE, "#000000",
				LocalDateTime.of(2030, 1, 1, 0, 0),
				LocalDateTime.of(2030, 1, 7, 0, 0));
		challenge.addCreator(memberId);
		challengeRepository.save(challenge);

		// when
		List<ChallengeResponse> challengeResponses = challengeService.retrieveInvitedChallengeList(memberId);

		// then
		assertThat(challengeResponses).isEmpty();
	}

	@Test
	void 초대_받은_멤버가_초대를_받으면_PENDING_에서_APPROVE_상태가_된다() {
		// given
		Challenge challenge = ChallengeCreator.create("운동하기", ChallengeType.EXERCISE, "#000000",
				LocalDateTime.of(2030, 1, 1, 0, 0),
				LocalDateTime.of(2030, 1, 7, 0, 0));
		challenge.addCreator(memberId);
		challenge.addPendingParticipator(friend.getId());
		challengeRepository.save(challenge);

		ParticipateChallengeRequest request = ParticipateChallengeRequest.testInstance(challenge.issueInvitationKey(memberId));

		// when
		challengeService.participateByInvitationKey(request, friend.getId());

		// then
		List<Challenge> challenges = challengeRepository.findAll();
		assertThat(challenges).hasSize(1);
		assertThat(challenges.get(0).getMembersCount()).isEqualTo(2);

		List<ChallengeMemberMapper> challengeMemberMappers = challengeMemberMapperRepository.findAll();
		assertThat(challengeMemberMappers).hasSize(2);
		assertThatChallengeMember(challengeMemberMappers.get(0), memberId, ChallengeRole.CREATOR, ChallengeMemberStatus.APPROVED);
		assertThatChallengeMember(challengeMemberMappers.get(1), friend.getId(), ChallengeRole.PARTICIPATOR, ChallengeMemberStatus.APPROVED);
	}

	@Test
	void 초대륿_받지_않은_사용자가_초대장을_통해_챌린지에_참여하는경우() {
		// given
		Challenge challenge = ChallengeCreator.create("운동하기", ChallengeType.EXERCISE, "#000000",
				LocalDateTime.of(2030, 1, 1, 0, 0),
				LocalDateTime.of(2030, 1, 7, 0, 0));
		challenge.addCreator(memberId);
		challengeRepository.save(challenge);

		ParticipateChallengeRequest request = ParticipateChallengeRequest.testInstance(challenge.issueInvitationKey(memberId));

		// when
		challengeService.participateByInvitationKey(request, friend.getId());

		// then
		List<Challenge> challenges = challengeRepository.findAll();
		assertThat(challenges).hasSize(1);
		assertThat(challenges.get(0).getMembersCount()).isEqualTo(2);

		List<ChallengeMemberMapper> challengeMemberMappers = challengeMemberMapperRepository.findAll();
		assertThat(challengeMemberMappers).hasSize(2);
		assertThatChallengeMember(challengeMemberMappers.get(0), memberId, ChallengeRole.CREATOR, ChallengeMemberStatus.APPROVED);
		assertThatChallengeMember(challengeMemberMappers.get(1), friend.getId(), ChallengeRole.PARTICIPATOR, ChallengeMemberStatus.APPROVED);
	}

}