package com.month.service.member;

import com.month.domain.member.Member;
import com.month.domain.member.MemberCreator;
import com.month.domain.member.MemberRepository;
import com.month.service.member.dto.request.MemberUpdateInfoRequest;
import com.month.service.member.dto.response.MemberInfoResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MemberServiceTest {

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private MemberService memberService;

	private Member member;

	@AfterEach
	void cleanUp() {
		memberRepository.deleteAll();
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
		MemberInfoResponse response = memberService.getMemberInfo(newMember.getId());

		// then
		assertThatMemberInfoResponse(response, newMember.getId(), email, name, photoUrl);
	}

	private static Stream<Arguments> sources_load_my_profile() {
		return Stream.of(
				Arguments.of("will.seungho@gmail.com", "강승호", "https://photoUrl.com", "seungho-uid"),
				Arguments.of("ksh980212@gmail.com", "will", "http://picture.com", "will-uid")
		);
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

	private void assertThatMemberInfoResponse(MemberInfoResponse response, Long id, String email, String name, String photoUrl) {
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
