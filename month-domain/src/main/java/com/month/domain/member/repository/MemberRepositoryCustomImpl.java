package com.month.domain.member.repository;

import com.month.domain.member.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static com.month.domain.member.QMember.member;

@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public Member findMemberById(Long memberId) {
		return queryFactory.selectFrom(member)
				.where(
						member.id.eq(memberId)
				).fetchOne();
	}

}