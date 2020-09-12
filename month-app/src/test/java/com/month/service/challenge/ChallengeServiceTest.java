package com.month.service.challenge;

import com.month.domain.challenge.*;
import com.month.service.MemberSetupTest;
import com.month.service.challenge.dto.request.ChallengeCreateInvitationKeyRequest;
import com.month.service.challenge.dto.request.ChallengeCreateRequest;
import com.month.service.challenge.dto.request.ChallengeInviteRequest;
import com.month.service.challenge.dto.request.ChallengeRetrieveRequest;
import com.month.service.challenge.dto.response.ChallengeInfoResponse;
import com.month.service.challenge.dto.response.ChallengeSimpleInfoResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class ChallengeServiceTest extends MemberSetupTest {

	@Autowired
	private ChallengeRepository challengeRepository;

	@Autowired
	private ChallengeMemberMapperRepository challengeMemberMapperRepository;

	@Autowired
	private ChallengeService challengeService;

	@AfterEach
	void cleanUp() {
		super.cleanup();
		challengeMemberMapperRepository.deleteAllInBatch();
		challengeRepository.deleteAllInBatch();
	}

	@Test
	void 새로운_챌린지를_생성한다() {
		// given
		String name = "운동합시다!";
		String description = "매일 운동하는 챌린지.";
		LocalDateTime startDateTime = LocalDateTime.of(2020, 9, 12, 0, 0);
		LocalDateTime endDateTime = LocalDateTime.of(2020, 10, 12, 0, 0);
		CertifyType certifyType = CertifyType.PICTURE;

		ChallengeCreateRequest request = ChallengeCreateRequest.testBuilder()
				.name(name)
				.description(description)
				.startDateTime(startDateTime)
				.endDateTime(endDateTime)
				.certifyType(certifyType)
				.build();

		// when
		challengeService.createNewChallenge(request, memberId);

		// then
		List<Challenge> challenges = challengeRepository.findAll();
		assertThat(challenges).hasSize(1);
		assertThat(challenges.get(0).getUuid()).isNotNull();
		assertThat(challenges.get(0).getMembersCount()).isEqualTo(1);
		assertChallengeInfo(challenges.get(0), name, description, startDateTime, endDateTime, certifyType);
	}

	@Test
	void 새로운_챌린지를_생성하면_자동으로_생성자가_된다() {
		// given
		String name = "운동합시다!";
		String description = "매일 운동하는 챌린지.";
		LocalDateTime startDateTime = LocalDateTime.of(2020, 9, 12, 0, 0);
		LocalDateTime endDateTime = LocalDateTime.of(2020, 10, 12, 0, 0);
		CertifyType certifyType = CertifyType.PICTURE;

		ChallengeCreateRequest request = ChallengeCreateRequest.testBuilder()
				.name(name)
				.description(description)
				.startDateTime(startDateTime)
				.endDateTime(endDateTime)
				.certifyType(certifyType)
				.build();

		// when
		challengeService.createNewChallenge(request, memberId);

		// then
		List<ChallengeMemberMapper> challengeMemberMappers = challengeMemberMapperRepository.findAll();
		assertThat(challengeMemberMappers).hasSize(1);
		assertThat(challengeMemberMappers.get(0).getMemberId()).isEqualTo(memberId);
		assertThat(challengeMemberMappers.get(0).getRole()).isEqualTo(ChallengeRole.CREATOR);
	}

	@Test
	void 챌린지의_정보를_불러온다() {
		// given
		String name = "챌린지";
		String description = "운동하는 챌린지";
		LocalDateTime startDateTime = LocalDateTime.of(2020, 9, 1, 0, 0);
		LocalDateTime endDateTime = LocalDateTime.of(2030, 9, 1, 0, 0, 0);
		CertifyType certifyType = CertifyType.PICTURE;

		Challenge challenge = ChallengeCreator.create(name, description, startDateTime, endDateTime, certifyType);
		challenge.addCreator(memberId);
		challengeRepository.save(challenge);

		ChallengeRetrieveRequest request = ChallengeRetrieveRequest.testInstance(challenge.getUuid());

		// when
		ChallengeInfoResponse response = challengeService.getDetailChallengeInfo(request, memberId);

		// then
		assertThatChallengeInfoResponse(response, name, description, startDateTime, endDateTime, certifyType);
	}

	@Test
	void 챌린지에_참가하지_않은_멤버는_챌린지_정보를_불러올수_없다() {
		// given
		Challenge challenge = ChallengeCreator.create("챌린지");
		challengeRepository.save(challenge);

		ChallengeRetrieveRequest request = ChallengeRetrieveRequest.testInstance(challenge.getUuid());

		// when & then
		assertThatThrownBy(() -> {
			challengeService.getDetailChallengeInfo(request, memberId);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void 내가_참가하고_있는_모든_챌린지를_불러온다() {
		// given
		Challenge challenge1 = ChallengeCreator.create("챌린지1");
		challenge1.addCreator(memberId);
		Challenge challenge2 = ChallengeCreator.create("챌린지2");
		challenge2.addParticipator(memberId);
		challengeRepository.saveAll(Arrays.asList(challenge1, challenge2));

		// when
		List<ChallengeSimpleInfoResponse> responses = challengeService.getMyChallengeInfo(memberId);

		// then
		assertThat(responses).hasSize(2);
		assertThat(responses.get(0).getName()).isEqualTo("챌린지1");
		assertThat(responses.get(1).getName()).isEqualTo("챌린지2");
	}

	@Test
	void 어떤_챌린지에도_참가하고_있지_않을때_나의_챌린지를_불러오면_빌_리스트가_반환된다() {
		// when
		List<ChallengeSimpleInfoResponse> responses = challengeService.getMyChallengeInfo(memberId);

		// then
		assertThat(responses).isEmpty();
		assertThat(responses).isNotNull();
	}

	@Test
	void 새로운_초대키를_발급한다() {
		// given
		Challenge challenge = ChallengeCreator.create("챌린지");
		challenge.addCreator(memberId);
		challengeRepository.save(challenge);

		ChallengeCreateInvitationKeyRequest request = ChallengeCreateInvitationKeyRequest.testInstance(challenge.getUuid());

		// when
		challengeService.createInvitationKey(request, memberId);

		// then
		List<Challenge> challenges = challengeRepository.findAll();
		assertThat(challenges).hasSize(1);
		assertThat(challenges.get(0).getInvitationKey()).isNotNull();
	}

	@Test
	void 챌린지에_참가중이지_않은_멤버는_초대키를_발급할_수없다() {
		// given
		Challenge challenge = ChallengeCreator.create("챌린지");
		challengeRepository.save(challenge);

		ChallengeCreateInvitationKeyRequest request = ChallengeCreateInvitationKeyRequest.testInstance(challenge.getUuid());

		// when & then
		assertThatThrownBy(() -> {
			challengeService.createInvitationKey(request, memberId);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void 초대키를_통해_새로운_멤버를_초대한다() {
		// given
		Challenge challenge = ChallengeCreator.create("챌린지");
		challenge.addCreator(memberId);
		challenge.createNewInvitationKey(memberId);
		challengeRepository.save(challenge);

		ChallengeInviteRequest request = ChallengeInviteRequest.testInstance(challenge.getInvitationKey());

		// when
		challengeService.inviteNewMemberWithInvitationKey(request, 2L);

		// then
		List<Challenge> challenges = challengeRepository.findAll();
		assertThat(challenges).hasSize(1);
		assertThat(challenges.get(0).getMembersCount()).isEqualTo(2);
	}

	@Test
	void 초대키를_통해_멤버가_초대되면_참가자로_합류한다() {
		// given
		Challenge challenge = ChallengeCreator.create("챌린지");
		challenge.addCreator(memberId);
		challenge.createNewInvitationKey(memberId);
		challengeRepository.save(challenge);

		ChallengeInviteRequest request = ChallengeInviteRequest.testInstance(challenge.getInvitationKey());

		// when
		challengeService.inviteNewMemberWithInvitationKey(request, 2L);

		// then
		List<ChallengeMemberMapper> challengeMemberMappers = challengeMemberMapperRepository.findAll();
		assertThat(challengeMemberMappers).hasSize(2);
		assertThat(challengeMemberMappers.get(1).getMemberId()).isEqualTo(2L);
		assertThat(challengeMemberMappers.get(1).getRole()).isEqualTo(ChallengeRole.PARTICIPATOR);
	}

	@Test
	void 이미_챌린저에_있는_멤버는_초대될_수_없다() {
		// given
		Challenge challenge = ChallengeCreator.create("챌린지");
		challenge.addCreator(memberId);
		challenge.createNewInvitationKey(memberId);
		challengeRepository.save(challenge);

		ChallengeInviteRequest request = ChallengeInviteRequest.testInstance(challenge.getInvitationKey());

		// when & then
		assertThatThrownBy(() -> {
			challengeService.inviteNewMemberWithInvitationKey(request, memberId);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void 초대키를_통해서_챌린지의_간단한_정보를_가져온다() {
		String name = "챌린지";
		String description = "운동하는 챌린지.";
		LocalDateTime startDateTime = LocalDateTime.of(2020, 9, 1, 0, 0);
		LocalDateTime endDateTime = LocalDateTime.of(2030, 9, 1, 0, 0, 0);
		CertifyType certifyType = CertifyType.PICTURE;

		Challenge challenge = ChallengeCreator.create(name, description, startDateTime, endDateTime, certifyType);
		challenge.addCreator(memberId);
		challenge.createNewInvitationKey(memberId);
		challengeRepository.save(challenge);

		// when
		ChallengeSimpleInfoResponse response = challengeService.getSimpleChallengeInfoByInvitationKey(challenge.getInvitationKey());

		// then
		assertThatChallengeSimpleInfoResponse(response, name, description, startDateTime, endDateTime, certifyType);
	}

	private void assertThatChallengeSimpleInfoResponse(ChallengeSimpleInfoResponse response, String name, String description, LocalDateTime startDateTime, LocalDateTime endDateTime, CertifyType certifyType) {
		assertThat(response.getName()).isEqualTo(name);
		assertThat(response.getDescription()).isEqualTo(description);
		assertThat(response.getStartDateTime()).isEqualTo(startDateTime);
		assertThat(response.getEndDateTime()).isEqualTo(endDateTime);
		assertThat(response.getCertifyType()).isEqualTo(certifyType);
	}

	private void assertThatChallengeInfoResponse(ChallengeInfoResponse response, String name, String description, LocalDateTime startDateTime, LocalDateTime endDateTime, CertifyType certifyType) {
		assertThat(response.getName()).isEqualTo(name);
		assertThat(response.getDescription()).isEqualTo(description);
		assertThat(response.getStartDateTime()).isEqualTo(startDateTime);
		assertThat(response.getEndDateTime()).isEqualTo(endDateTime);
		assertThat(response.getCertifyType()).isEqualTo(certifyType);
	}

	private void assertChallengeInfo(Challenge challenge, String name, String description, LocalDateTime startDateTime, LocalDateTime endDateTime, CertifyType certifyType) {
		assertThat(challenge.getName()).isEqualTo(name);
		assertThat(challenge.getDescription()).isEqualTo(description);
		assertThat(challenge.getStartDateTime()).isEqualTo(startDateTime);
		assertThat(challenge.getEndDateTime()).isEqualTo(endDateTime);
		assertThat(challenge.getCertifyType()).isEqualTo(certifyType);
	}

}