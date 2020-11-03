package com.month.domain.friend.repository;

import com.month.domain.friend.FriendMapper;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.month.domain.friend.QFriendMapper.friendMapper;

@RequiredArgsConstructor
public class FriendMapperRepositoryCustomImpl implements FriendMapperRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public FriendMapper findFriendMapper(Long memberId, Long targetMemberId) {
		return queryFactory.selectFrom(friendMapper)
				.where(
						friendMapper.memberId.eq(memberId),
						friendMapper.targetMemberId.eq(targetMemberId)
				).fetchOne();
	}

	@Override
	public List<FriendMapper> findAllByMemberId(Long memberId) {
		return queryFactory.selectFrom(friendMapper)
				.where(
						friendMapper.memberId.eq(memberId)
				).fetch();
	}

}
