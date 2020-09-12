package com.month.service;

import com.month.domain.member.Member;
import com.month.domain.member.MemberCreator;
import com.month.domain.member.MemberRepository;
import com.month.service.auth.AuthService;
import com.month.service.auth.dto.request.AuthRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AuthServiceTest {

	@Autowired
	private AuthService authService;

	@Autowired
	private MemberRepository memberRepository;

	private Member member;

	@AfterEach
	void cleanUp() {
		memberRepository.deleteAll();
	}

	@BeforeEach
	void setUpMember() {
		member = MemberCreator.create("will.seungho@gmail.com", "강승호", "idToken");
	}

	@Test
	void 새로운_멤버가_로그인_요청한경우_먼저_회원가입이_진행된다() {
		// given
		String email = "will.seungho@gmail.com";
		String name = "강승호";
		String photoUrl = "https://photo.url";
		String providerId = "providerId";
		String idToken = "idToken";

		AuthRequest request = AuthRequest.testBuilder()
				.email(email)
				.name(name)
				.photoUrl(photoUrl)
				.providerId(providerId)
				.idToken(idToken)
				.build();

		// when
		authService.handleAuthentication(request);

		// then
		List<Member> members = memberRepository.findAll();
		assertThat(members).hasSize(1);
		assertMemberInfo(members.get(0), email, name, photoUrl, providerId, idToken);
	}

	@Test
	void 새로운_멤버가_로그인_요청한경우_회원가입이_진행되고_로그인이_진행된다() {
		// given
		String email = "will.seungho@gmail.com";
		String name = "강승호";
		String photoUrl = "https://photo.url";
		String providerId = "providerId";
		String idToken = "idToken";

		AuthRequest request = AuthRequest.testBuilder()
				.email(email)
				.name(name)
				.photoUrl(photoUrl)
				.providerId(providerId)
				.idToken(idToken)
				.build();

		// when
		Long memberId = authService.handleAuthentication(request);

		// then
		List<Member> members = memberRepository.findAll();
		assertThat(members).hasSize(1);
		assertThat(memberId).isEqualTo(members.get(0).getId());
	}

	@Test
	void 기존의_멤버가_로그인을_요청한경우_로그인이_진행된다() {
		// given
		memberRepository.save(member);

		AuthRequest request = AuthRequest.testBuilder()
				.email(member.getEmail())
				.idToken(member.getIdToken())
				.build();

		// when
		Long memberId = authService.handleAuthentication(request);

		// then
		List<Member> members = memberRepository.findAll();
		assertThat(members).hasSize(1);
		assertThat(memberId).isEqualTo(members.get(0).getId());
	}

	private void assertMemberInfo(Member member, String email, String name, String photoUrl, String providerId, String idToken) {
		assertThat(member.getEmail()).isEqualTo(email);
		assertThat(member.getName()).isEqualTo(name);
		assertThat(member.getPhotoUrl()).isEqualTo(photoUrl);
		assertThat(member.getPhotoUrl()).isEqualTo(photoUrl);
		assertThat(member.getProviderId()).isEqualTo(providerId);
		assertThat(member.getIdToken()).isEqualTo(idToken);
	}

}