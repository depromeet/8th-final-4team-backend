package com.month.domain.accreditation.repository;

import com.month.domain.accreditation.Accreditation;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.month.domain.accreditation.QAccreditation.accreditation;

@RequiredArgsConstructor
public class AccreditationRepositoryCustomImpl implements AccreditationRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<Accreditation> findAllByChallengeUuidList(List<String> challengesUuidList) {
		return queryFactory.selectFrom(accreditation)
				.where(
						accreditation.challengeUuid.in(challengesUuidList)
				).fetch();
	}

}
