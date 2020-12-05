package com.month.service.auth;

import com.month.domain.member.Member;
import com.month.domain.member.MemberCreator;
import com.month.domain.member.MemberRepository;
import com.month.service.auth.dto.request.AuthRequest;
import com.month.external.firebase.FirebaseUtils;
import com.month.external.firebase.dto.CustomFirebaseToken;
import com.month.service.auth.dto.request.SignUpRequest;
import com.month.service.auth.dto.response.AuthResponse;
import com.month.type.AuthType;
import com.month.utils.jwt.JwtTokenProvider;
import com.month.utils.jwt.dto.SignUpToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.servlet.http.HttpSession;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AuthServiceTest {

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private HttpSession httpSession;

	@AfterEach
	void cleanUp() {
		memberRepository.deleteAll();
	}

	private AuthService authService;

	@BeforeEach
	void setUpMemberInfo() {
		authService = new AuthService(memberRepository, new StubFirebaseUtils(), new StubJwtTokenProvider(), httpSession);
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

	private static class StubJwtTokenProvider implements JwtTokenProvider {
		@Override
		public String encodeSignUpToken(String idToken, String email) {
			return "token";
		}

		@Override
		public SignUpToken decodeSignUpToken(String token) {
			return SignUpToken.newInstance("idToken", "email");
		}
	}

	@Test
	void 로그인_요청시_회원가입한_멤버가_존재하지_않으면_회원가입을_위한_정보가_반환된() {
		// given
		String idToken = "idToken";

		AuthRequest request = AuthRequest.testBuilder()
				.idToken(idToken)
				.build();

		// when
		AuthResponse response = authService.handleAuthentication(request);

		// then
		assertAuthResponse(response, AuthType.SIGN_UP, "token", "강승호", "picture");
		assertThat(response.getLoginSessionId()).isNull();
	}

	@Test
	void 회원가입한_멤버가_존재하면_로그인이_진행된다() {
		// given
		memberRepository.save(MemberCreator.create("will.seungho@gmail.com", "승호강", null, "uid"));

		AuthRequest request = AuthRequest.testBuilder()
				.idToken("idToken")
				.build();

		// when
		AuthResponse response = authService.handleAuthentication(request);

		// then
		assertAuthResponse(response, AuthType.LOGIN, null, null, null);
		assertThat(response.getLoginSessionId()).isNotNull();
	}

	@Test
	void 회원가입을_요청하는_경우_새로운_멤버가_생성된() {
		// given
		String name = "jello";
		String photoUrl = "https://photo.jello.com";

		SignUpRequest request = SignUpRequest.testBuilder()
				.signUpToken("token")
				.name(name)
				.photoUrl(photoUrl)
				.build();

		// when
		authService.signUpMember(request);

		// then
		List<Member> members = memberRepository.findAll();
		assertThat(members).hasSize(1);
		assertMemberInfo(members.get(0), "will.seungho@gmail.com", name, photoUrl, "uid");
	}

	private void assertAuthResponse(AuthResponse response, AuthType type, String signUpToken, String name, String photoUrl) {
		assertThat(response.getType()).isEqualTo(type);
		assertThat(response.getSignUpToken()).isEqualTo(signUpToken);
		assertThat(response.getName()).isEqualTo(name);
		assertThat(response.getPhotoUrl()).isEqualTo(photoUrl);
	}

	private void assertMemberInfo(Member member, String email, String name, String photoUrl, String uid) {
		assertThat(member.getEmail()).isEqualTo(email);
		assertThat(member.getName()).isEqualTo(name);
		assertThat(member.getPhotoUrl()).isEqualTo(photoUrl);
		assertThat(member.getUid()).isEqualTo(uid);
	}

}