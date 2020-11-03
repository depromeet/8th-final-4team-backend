package com.month.service.member;

import com.month.domain.member.Member;
import com.month.domain.member.MemberRepository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberServiceUtils {

	public static Member findMemberById(MemberRepository memberRepository, Long memberId) {
		Member member = memberRepository.findMemberId(memberId);
		if (member == null) {
			throw new IllegalArgumentException(String.format("해당 멤버 (%s) 는 존재하지 않습니다.", memberId));
		}
		return member;
	}

	public static Member findMemberByEmail(MemberRepository memberRepository, String email) {
		Member member = memberRepository.findMemberByEmail(email);
		if (member == null) {
			throw new IllegalArgumentException(String.format("해당 멤버 (%s) 는 존재하지 않습니다.", email));
		}
		return member;
	}

	public static void validateNotExistMember(MemberRepository memberRepository, String uid) {
		if (memberRepository.findMemberByUid(uid) != null) {
			throw new IllegalArgumentException(String.format("이미 존재하는 회원 (%s) 입니다.", uid));
		}
	}

}
