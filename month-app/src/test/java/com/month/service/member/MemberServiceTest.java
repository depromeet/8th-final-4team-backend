package com.month.service.member;

import com.month.domain.accreditation.Accreditation;
import com.month.domain.accreditation.AccreditationCreator;
import com.month.domain.accreditation.repository.AccreditationRepository;
import com.month.domain.challenge.Challenge;
import com.month.domain.challenge.ChallengeCreator;
import com.month.domain.challenge.ChallengeRepository;
import com.month.domain.member.Member;
import com.month.domain.member.MemberCreator;
import com.month.domain.member.MemberRepository;
import com.month.service.member.dto.request.MemberUpdateInfoRequest;
import com.month.service.member.dto.response.MemberDetailInfoResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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

@SpringBootTest
class MemberServiceTest {

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private MemberService memberService;

	@Autowired
	private ChallengeRepository challengeRepository;

	@Autowired
	private AccreditationRepository accreditationRepository;

	private Member member;

	@AfterEach
	void cleanUp() {
		memberRepository.deleteAll();
		challengeRepository.deleteAll();
		accreditationRepository.deleteAll();
	}

	@BeforeEach
	void setUpMember() {
		member = MemberCreator.create("will.seungho@gmail.com", "강승호", "photoUrl", "uid");
	}

	@MethodSource("sources_load_my_profile")
	@ParameterizedTest
	void 나의_회원_정보를_정상적으로_불러온다(String email, String name, String photoUrl, String uid) {
		// given
		Member newMember = MemberCreator.create(email, name, photoUrl, uid);
		memberRepository.save(newMember);

		// when
		MemberDetailInfoResponse response = memberService.getMemberInfo(newMember.getId());

		// then
		assertThatMemberDetailInfoResponse(response, newMember.getId(), email, name, photoUrl);
	}

	private static Stream<Arguments> sources_load_my_profile() {
		return Stream.of(
				Arguments.of("will.seungho@gmail.com", "강승호", "https://photoUrl.com", "seungho-uid"),
				Arguments.of("ksh980212@gmail.com", "will", "http://picture.com", "will-uid")
		);
	}

//	@Test
//	void 내가_도전한_횟수를_가져온다() {
//		// given
//		Member member = MemberCreator.create("will.seungho@gmail.com");
//		memberRepository.save(member);
//
//		Challenge challenge1 = ChallengeCreator.create("챌린지1",
//				LocalDateTime.of(2020, 10, 14, 0, 0),
//				LocalDateTime.of(2020, 11, 14, 0, 0));
//		challenge1.addCreator(member.getId());
//
//		Challenge challenge2 = ChallengeCreator.create("챌린지2",
//				LocalDateTime.of(2020, 10, 14, 0, 0),
//				LocalDateTime.of(2020, 11, 14, 0, 0));
//		challenge2.addParticipator(member.getId());
//
//		challengeRepository.saveAll(Arrays.asList(challenge1, challenge2));
//
//		// when
//		MemberDetailInfoResponse response = memberService.getMemberInfo(member.getId());
//
//		// then
//		assertThat(response.getTotalChallengesCount()).isEqualTo(2);
//	}
//
//	@Test
//	void 나의_정보를_반환할때_나의_달성_퍼센트를_계산해서_같이_반환한다() {
//		// given
//		Member member = MemberCreator.create("will.seungho@gmail.com");
//		memberRepository.save(member);
//
//		Challenge challenge = ChallengeCreator.create("챌린지1",
//				LocalDateTime.of(2019, 10, 14, 0, 0),
//				LocalDateTime.of(2019, 10, 22, 0, 0));
//		challenge.addCreator(member.getId());
//		challengeRepository.save(challenge);
//
//		Accreditation accreditation1 = AccreditationCreator.create(member.getId(), challenge.getUuid());
//		Accreditation accreditation2 = AccreditationCreator.create(member.getId(), challenge.getUuid());
//		accreditationRepository.saveAll(Arrays.asList(accreditation1, accreditation2));
//
//		// when
//		MemberDetailInfoResponse response = memberService.getMemberInfo(member.getId());
//
//		// then
//		assertThat(response.getAchieveChallengeRate()).isEqualTo(25.0);
//	}

	@Test
	void 어떤_챌린지도_하지않은경우_총_도전수_0_되고_달성률_0_가된다() {
		// given
		Member member = MemberCreator.create("will.seungho@gmail.com");
		memberRepository.save(member);

		// when
		MemberDetailInfoResponse response = memberService.getMemberInfo(member.getId());

		// then
		assertThat(response.getTotalChallengesCount()).isEqualTo(0);
		assertThat(response.getAchieveChallengeRate()).isEqualTo(0);
	}

	@Test
	void 나의_회원정보를_수정한다() {
		// given
		memberRepository.save(member);

		String name = "승호강";
		String photoUrl = "changed";

		MemberUpdateInfoRequest request = MemberUpdateInfoRequest.testInstance(name, photoUrl);

		// when
		memberService.updateMemberInfo(request, member.getId());

		// then
		List<Member> members = memberRepository.findAll();
		assertThat(members).hasSize(1);
		assertThatMemberInfo(members.get(0), member.getEmail(), name, photoUrl, member.getUid());
	}

	private void assertThatMemberDetailInfoResponse(MemberDetailInfoResponse response, Long id, String email, String name, String photoUrl) {
		assertThat(response.getId()).isEqualTo(id);
		assertThat(response.getEmail()).isEqualTo(email);
		assertThat(response.getName()).isEqualTo(name);
		assertThat(response.getPhotoUrl()).isEqualTo(photoUrl);
	}

	private void assertThatMemberInfo(Member member, String email, String name, String photoUrl, String uid) {
		assertThat(member.getEmail()).isEqualTo(email);
		assertThat(member.getName()).isEqualTo(name);
		assertThat(member.getPhotoUrl()).isEqualTo(photoUrl);
		assertThat(member.getUid()).isEqualTo(uid);
	}

}
