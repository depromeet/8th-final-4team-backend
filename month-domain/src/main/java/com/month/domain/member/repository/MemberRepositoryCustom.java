package com.month.domain.member.repository;

import com.month.domain.member.Member;

public interface MemberRepositoryCustom {

	Member findMemberById(Long memberId);

}