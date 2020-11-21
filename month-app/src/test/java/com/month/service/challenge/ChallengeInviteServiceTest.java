package com.month.service.challenge;

import com.month.domain.challenge.*;
import com.month.domain.member.Member;
import com.month.domain.member.MemberCreator;
import com.month.domain.member.MemberRepository;
import com.month.exception.NotAllowedException;
import com.month.exception.NotFoundException;
import com.month.service.MemberSetupTest;
import com.month.service.challenge.dto.request.*;
import com.month.service.challenge.dto.response.ChallengeResponse;
import com.month.service.challenge.dto.response.InvitedChallengeListResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class ChallengeInviteServiceTest extends MemberSetupTest {

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
	void 초대를_받은_사용자도_초대키를_발급받을_수_있다() {
		// given
		Challenge challenge = ChallengeCreator.create("챌린지",
				LocalDateTime.of(2030, 1, 1, 0, 0),
				LocalDateTime.of(2030, 1, 7, 0, 0));
		challenge.addPendingParticipator(memberId);
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
		List<InvitedChallengeListResponse> challengeResponses = challengeService.retrieveInvitedChallengeList(memberId);

		// then
		assertThat(challengeResponses).hasSize(1);
		assertThat(challengeResponses.get(0).getInvitationKey()).isEqualTo(challenge.issueInvitationKey(memberId));
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
		List<InvitedChallengeListResponse> challengeResponses = challengeService.retrieveInvitedChallengeList(memberId);

		// then
		assertThat(challengeResponses).isEmpty();
	}

	@Test
	void 초대_받은_멤버가_초대를_받으면_PENDING_에서_APPROVE_상태가_된다() {
		// given
		Challenge challenge = ChallengeCreator.create("운동하기", ChallengeType.EXERCISE, "#000000",
				LocalDateTime.of(2030, 1, 1, 0, 0),
				LocalDateTime.of(2030, 1, 7, 0, 0));
		challenge.addPendingParticipator(memberId);
		challengeRepository.save(challenge);

		ParticipateChallengeRequest request = ParticipateChallengeRequest.testInstance(challenge.issueInvitationKey(memberId));

		// when
		challengeService.participateByInvitationKey(request, memberId);

		// then
		List<Challenge> challenges = challengeRepository.findAll();
		assertThat(challenges).hasSize(1);
		assertThat(challenges.get(0).getMembersCount()).isEqualTo(1);

		List<ChallengeMemberMapper> challengeMemberMappers = challengeMemberMapperRepository.findAll();
		assertThat(challengeMemberMappers).hasSize(1);
		ChallengeServiceTestUtils.assertThatChallengeMember(challengeMemberMappers.get(0), memberId, ChallengeRole.PARTICIPATOR, ChallengeMemberStatus.APPROVED);
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
		ChallengeServiceTestUtils.assertThatChallengeMember(challengeMemberMappers.get(0), memberId, ChallengeRole.CREATOR, ChallengeMemberStatus.APPROVED);
		ChallengeServiceTestUtils.assertThatChallengeMember(challengeMemberMappers.get(1), friend.getId(), ChallengeRole.PARTICIPATOR, ChallengeMemberStatus.APPROVED);
	}

	@Test
	void 초대_받은_사용자가_초대를_거절하면_PENDING_에서_REJECT_상태가된다() {
		// given
		Challenge challenge = ChallengeCreator.create("운동하기", ChallengeType.EXERCISE, "#000000",
				LocalDateTime.of(2030, 1, 1, 0, 0),
				LocalDateTime.of(2030, 1, 7, 0, 0));
		challenge.addPendingParticipator(memberId);
		challengeRepository.save(challenge);

		RejectInviteChallengeRequest request = RejectInviteChallengeRequest.testInstance(challenge.issueInvitationKey(memberId));

		// when
		challengeService.rejectInvitation(request, memberId);

		// then
		List<Challenge> challenges = challengeRepository.findAll();
		assertThat(challenges).hasSize(1);
		assertThat(challenges.get(0).getMembersCount()).isEqualTo(0);

		List<ChallengeMemberMapper> challengeMemberMappers = challengeMemberMapperRepository.findAll();
		assertThat(challengeMemberMappers).hasSize(1);
		ChallengeServiceTestUtils.assertThatChallengeMember(challengeMemberMappers.get(0), memberId, ChallengeRole.PARTICIPATOR, ChallengeMemberStatus.REJECT);
	}

}