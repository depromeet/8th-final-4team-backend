package com.month.service.member;

import com.month.domain.member.Member;
import com.month.domain.member.MemberCreator;
import com.month.domain.member.MemberRepository;
import com.month.service.member.dto.request.MemberUpdateInfoRequest;
import com.month.service.member.dto.response.MemberInfoResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

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

	@Test
	void 나의_정보를_불러온다() {
		// given
		memberRepository.save(member);

		// when
		MemberInfoResponse response = memberService.getMemberInfo(member.getId());

		// then
		assertThatMemberInfoResponse(response, member.getId(), member.getEmail(), member.getName(), member.getPhotoUrl());
	}

	@Test
	void 나의_정보를_수정한다() {
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
