package com.month.service.auth;

import com.month.domain.member.MemberCreator;
import com.month.domain.member.MemberRepository;
import com.month.service.auth.dto.request.AuthRequest;
import com.month.external.firebase.FirebaseUtils;
import com.month.external.firebase.dto.CustomFirebaseToken;
import com.month.service.auth.dto.response.AuthResponse;
import com.month.type.AuthType;
import com.month.utils.jwt.JwtTokenProvider;
import com.month.utils.jwt.dto.JwtToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.servlet.http.HttpSession;

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
		public String createToken(String idToken, String email) {
			return "token";
		}

		@Override
		public JwtToken decodeToken(String token) {
			return JwtToken.newInstance("idToken", "email");
		}
	}

	@Test
	void 기존의_멤버가_존재하지_않으면_회원가입이_진행된다() {
		// given
		String idToken = "idToken";

		AuthRequest request = AuthRequest.testBuilder()
				.idToken(idToken)
				.build();

		// when
		AuthResponse response = authService.handleAuthentication(request);

		// then
		assertAuthResponse(response, AuthType.SIGN_UP, "token", "강승호", "picture", null);
	}

	@Test
	void 기존의_멤버가_존재하면_로그인이_진행된다() {
		// given
		memberRepository.save(MemberCreator.create("will.seungho@gmail.com", "승호강", null, "uid"));

		AuthRequest request = AuthRequest.testBuilder()
				.idToken("idToken")
				.build();

		// when
		AuthResponse response = authService.handleAuthentication(request);

		// then
		assertAuthResponse(response, AuthType.LOGIN, null, null, null, "1");
	}

	private void assertAuthResponse(AuthResponse response, AuthType type, String token, String name, String photoUrl, String sessionId) {
		assertThat(response.getType()).isEqualTo(type);
		assertThat(response.getToken()).isEqualTo(token);
		assertThat(response.getName()).isEqualTo(name);
		assertThat(response.getPhotoUrl()).isEqualTo(photoUrl);
		assertThat(response.getSessionId()).isEqualTo(sessionId);
	}

}