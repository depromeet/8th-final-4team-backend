package com.month.service;

import com.month.domain.member.Member;
import com.month.domain.member.MemberCreator;
import com.month.domain.member.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public abstract class MemberSetupTest {

	@Autowired
	private MemberRepository memberRepository;

	protected Long memberId;

	@BeforeEach
	void setUp() {
		Member member = MemberCreator.create("will.seungho@gmail.com");
		memberRepository.save(member);
		memberId = member.getId();
	}

	protected void cleanup() {
		memberRepository.deleteAll();
	}
	
}
