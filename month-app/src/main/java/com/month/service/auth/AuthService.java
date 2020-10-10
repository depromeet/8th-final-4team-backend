package com.month.service.auth;

import com.month.external.firebase.FirebaseUtils;
import com.month.external.firebase.dto.CustomFirebaseToken;
import com.month.domain.member.Member;
import com.month.domain.member.MemberRepository;
import com.month.service.auth.dto.request.AuthRequest;
import com.month.service.auth.dto.response.AuthResponse;
import com.month.service.auth.dto.request.SignUpMemberRequest;
import com.month.service.member.MemberServiceUtils;
import com.month.type.session.MemberSession;
import com.month.utils.jwt.JwtTokenProvider;
import com.month.utils.jwt.dto.JwtToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;

import static com.month.type.session.SessionConstants.LOGIN_SESSION;

@RequiredArgsConstructor
@Service
public class AuthService {

	private final MemberRepository memberRepository;
	private final FirebaseUtils firebaseUtils;
	private final JwtTokenProvider jwtTokenProvider;
	private final HttpSession httpSession;

	@Transactional(readOnly = true)
	public AuthResponse handleAuthentication(AuthRequest request) {
		CustomFirebaseToken firebaseToken = firebaseUtils.getDecodedToken(request.getIdToken());
		Member findMember = memberRepository.findMemberByUid(firebaseToken.getUid());
		if (findMember == null) {
			// 1. 해당 회원 정보가 없는 경우, 회원가입 진행을 위한 토큰 및 유저 정보 반환한다.
			String signUpToken = jwtTokenProvider.createToken(request.getIdToken(), firebaseToken.getEmail());
			return AuthResponse.signUp(signUpToken, firebaseToken.getName(), firebaseToken.getPhotoUrl());
		}
		// 2. 회원 정보가 있을 경우, 로그인을 진행한다.
		httpSession.setAttribute(LOGIN_SESSION, MemberSession.of(findMember.getId()));
		return AuthResponse.login(httpSession.getId());
	}

	@Transactional
	public AuthResponse signUpMember(SignUpMemberRequest request) {
		JwtToken signUpToken = jwtTokenProvider.decodeToken(request.getSignUpToken());
		CustomFirebaseToken firebaseToken = firebaseUtils.getDecodedToken(signUpToken.getIdToken());

		MemberServiceUtils.validateNotExistMember(memberRepository, firebaseToken.getUid());
		Member newMember = memberRepository.save(firebaseToken.toEntity(request.getName(), request.getPhotoUrl()));

		httpSession.setAttribute(LOGIN_SESSION, MemberSession.of(newMember.getId()));
		return AuthResponse.login(httpSession.getId());
	}

}
