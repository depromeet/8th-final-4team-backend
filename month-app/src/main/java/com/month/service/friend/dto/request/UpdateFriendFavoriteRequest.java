package com.month.service.friend.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateFriendFavoriteRequest {

	private Long friendMemberId;

	private boolean isFavorite;

	@Builder(builderClassName = "TestBuilder", builderMethodName = "testBuilder")
	public UpdateFriendFavoriteRequest(Long friendMemberId, boolean isFavorite) {
		this.friendMemberId = friendMemberId;
		this.isFavorite = isFavorite;
	}

}
