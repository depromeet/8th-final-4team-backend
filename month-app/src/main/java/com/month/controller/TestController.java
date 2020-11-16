package com.month.controller;

import com.month.domain.member.Member;
import com.month.domain.member.MemberCreator;
import com.month.domain.member.MemberRepository;
import com.month.exception.ConflictException;
import com.month.type.session.MemberSession;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

import static com.month.exception.type.ExceptionDescriptionType.MEMBER;
import static com.month.type.session.SessionConstants.LOGIN_SESSION;

// TODO 프론트와 인증 연동후 삭제하기 (배포시 ?)
@Profile("local")
@RequiredArgsConstructor
@RestController
public class TestController {

	private final HttpSession httpSession;
	private final MemberRepository memberRepository;

	@GetMapping("/test-auth")
	public String testAuth() {
		String email = "test@test.com";
		Member member = memberRepository.findMemberByEmail(email);
		if (member == null) {
			member = memberRepository.save(MemberCreator.create(email, "name", null, "uid"));
		}
		httpSession.setAttribute(LOGIN_SESSION, MemberSession.of(member.getId()));
		return httpSession.getId();
	}

	@GetMapping("/test-auth/custom")
	public String testAuth(@RequestParam String email) {
		if (memberRepository.findMemberByEmail(email) != null) {
			throw new ConflictException("이미 존재하는 멤버", MEMBER);
		}
		Member member = memberRepository.save(MemberCreator.create(email, "테스트 계정", null, email + "uid"));
		httpSession.setAttribute(LOGIN_SESSION, MemberSession.of(member.getId()));
		return httpSession.getId();
	}

}
