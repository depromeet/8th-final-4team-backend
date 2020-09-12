package com.month.service.member;

import com.month.domain.member.Member;
import com.month.domain.member.MemberRepository;

final class MemberServiceUtils {

	static Member findMemberById(MemberRepository memberRepository, Long memberId) {
		Member member = memberRepository.findMemberId(memberId);
		if (member == null) {
			throw new IllegalArgumentException(String.format("해당 멤버 (%s) 는 존재하지 않습니다.", memberId));
		}
		return member;
	}

}
