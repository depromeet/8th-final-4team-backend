package com.month.domain.member.repository;

import com.month.domain.member.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static com.month.domain.member.QMember.member;

@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public Member findMemberByTokenAndEmail(String idToken, String email) {
		return queryFactory.selectFrom(member)
				.where(
						member.idToken.eq(idToken),
						member.email.email.eq(email)
				).fetchOne();
	}

	@Override
	public Member findMemberId(Long memberId) {
		return queryFactory.selectFrom(member)
				.where(
						member.id.eq(memberId)
				).fetchOne();
	}

}