package com.month.domain.member.repository;

import com.month.domain.member.Member;

public interface MemberRepositoryCustom {

	Member findMemberId(Long memberId);

	Member findMemberByUid(String uid);

}