package com.month.service.friend.dto.response;

import com.month.domain.friend.FriendMapper;
import com.month.domain.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class FriendMemberDetailInfoResponse {

	private final Long id;

	private final String email;

	private final String name;

	private final String photoUrl;

	private final boolean isFavorite;

	private final int totalChallengesCountWithFriend;

	private final double achieveChallengeRate;

	public static FriendMemberDetailInfoResponse of(Member member, FriendMapper friendMapper,
													int totalChallengesCountWithFriend, double achieveChallengeRate) {
		return new FriendMemberDetailInfoResponse(member.getId(), member.getEmail(), member.getName(), member.getPhotoUrl(),
				friendMapper.isFavorite(), totalChallengesCountWithFriend, achieveChallengeRate);
	}

}

