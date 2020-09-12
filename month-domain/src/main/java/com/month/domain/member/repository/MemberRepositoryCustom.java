package com.month.domain.member.repository;

import com.month.domain.member.Member;

import java.util.List;

public interface MemberRepositoryCustom {

	Member findMemberByTokenAndEmail(String idToken, String email);

	Member findMemberId(Long memberId);

	List<Member> findMembersByMemberIds(List<Long> memberIds);

}