package com.month.domain.member.repository;

import com.month.domain.member.Member;

public interface MemberRepositoryCustom {

	Member findMemberByTokenAndEmail(String idToken, String email);

	Member findMemberId(Long memberId);
	
}