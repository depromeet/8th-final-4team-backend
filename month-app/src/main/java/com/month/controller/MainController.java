package com.month.controller;

import com.month.domain.member.Member;
import com.month.domain.member.MemberCreator;
import com.month.domain.member.MemberRepository;
import com.month.type.session.MemberSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

import static com.month.type.session.SessionConstants.LOGIN_SESSION;

@RequiredArgsConstructor
@RestController
public class MainController {

	private final HttpSession httpSession;
	private final MemberRepository memberRepository;

	@GetMapping("/ping")
	public String ping() {
		return "pong";
	}

	// TODO Prod 배포시 지우기
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

	// TODO 소셜 로그인 Integration 하고 지우기
	@GetMapping("/test-auth/custom")
	public String testAuth(@RequestParam String email) {
		if (memberRepository.findMemberByEmail(email) != null) {
			throw new IllegalArgumentException("이미 존재하는 멤버");
		}
		Member member = memberRepository.save(MemberCreator.create(email, "테스트 계정", null, email + "uid"));
		httpSession.setAttribute(LOGIN_SESSION, MemberSession.of(member.getId()));
		return httpSession.getId();
	}

}