package com.month.service.member;

import com.month.domain.member.Member;
import com.month.domain.member.MemberRepository;
import com.month.exception.ConflictException;
import com.month.exception.NotFoundException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static com.month.exception.type.ExceptionDescriptionType.MEMBER;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberServiceUtils {

	static Member findMemberById(MemberRepository memberRepository, Long memberId) {
		Member member = memberRepository.findMemberId(memberId);
		if (member == null) {
			throw new NotFoundException(String.format("해당 멤버 (%s) 는 존재하지 않습니다.", memberId), MEMBER);
		}
		return member;
	}

	public static Member findMemberByEmail(MemberRepository memberRepository, String email) {
		Member member = memberRepository.findMemberByEmail(email);
		if (member == null) {
			throw new NotFoundException(String.format("해당 멤버 (%s) 는 존재하지 않습니다.", email), MEMBER);
		}
		return member;
	}

	public static void validateNotExistMember(MemberRepository memberRepository, String uid) {
		if (memberRepository.findMemberByUid(uid) != null) {
			throw new ConflictException(String.format("이미 존재하는 회원 (%s) 입니다.", uid), MEMBER);
		}
	}

}
