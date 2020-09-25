package com.month.service.auth;

import com.month.domain.member.Member;
import com.month.domain.member.MemberCreator;
import com.month.domain.member.MemberRepository;
import com.month.service.auth.dto.request.AuthRequest;
import com.month.external.firebase.FirebaseUtils;
import com.month.external.firebase.dto.CustomFirebaseToken;
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
	private MemberRepository memberRepository;

	@AfterEach
	void cleanUp() {
		memberRepository.deleteAll();
	}

	private AuthService authService;

	@BeforeEach
	void setUpMemberInfo() {
		authService = new AuthService(memberRepository, new StubFirebaseUtils());
	}

	private static class StubFirebaseUtils implements FirebaseUtils {
		@Override
		public CustomFirebaseToken getDecodedToken(String idToken) {
			return CustomFirebaseToken.builder()
					.email("will.seungho@gmail.com")
					.name("강승호")
					.uid("uid")
					.photoUrl("picture")
					.build();
		}
	}

	@Test
	void 새로운_멤버가_로그인_요청한경우_먼저_회원가입이_진행된다() {
		// given
		AuthRequest request = AuthRequest.testBuilder()
				.idToken("idToken")
				.build();

		// when
		authService.handleAuthentication(request);

		// then
		List<Member> members = memberRepository.findAll();
		assertThat(members).hasSize(1);
		assertMemberInfo(members.get(0), "will.seungho@gmail.com", "강승호", "picture", "uid");
	}

	@Test
	void 새로운_멤버가_로그인_요청한경우_회원가입이_진행되고_로그인이_진행된다() {
		// given
		String idToken = "idToken";

		AuthRequest request = AuthRequest.testBuilder()
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
		memberRepository.save(MemberCreator.create("will.seungho@gmail.com", "승호강", null, "uid"));

		AuthRequest request = AuthRequest.testBuilder()
				.idToken("idToken")
				.build();

		// when
		Long memberId = authService.handleAuthentication(request);

		// then
		List<Member> members = memberRepository.findAll();
		assertThat(members).hasSize(1);
		assertThat(memberId).isEqualTo(members.get(0).getId());
	}

	private void assertMemberInfo(Member member, String email, String name, String photoUrl, String uid) {
		assertThat(member.getEmail()).isEqualTo(email);
		assertThat(member.getName()).isEqualTo(name);
		assertThat(member.getPhotoUrl()).isEqualTo(photoUrl);
		assertThat(member.getPhotoUrl()).isEqualTo(photoUrl);
		assertThat(member.getUid()).isEqualTo(uid);
	}

}