package com.month.controller;

import com.month.domain.member.Member;
import com.month.domain.member.MemberCreator;
import com.month.domain.member.MemberRepository;
import com.month.type.session.MemberSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
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
		Member member = memberRepository.save(MemberCreator.create("test@test.com", "name", null, "uid"));
		httpSession.setAttribute(LOGIN_SESSION, MemberSession.of(member.getId()));
		return httpSession.getId();
	}

}