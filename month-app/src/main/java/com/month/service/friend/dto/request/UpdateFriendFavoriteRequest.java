package com.month.service.friend.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class UpdateFriendFavoriteRequest {

	@NotNull
	private Long friendMemberId;

	@NotNull
	private Boolean isFavorite;

	@Builder(builderClassName = "TestBuilder", builderMethodName = "testBuilder")
	public UpdateFriendFavoriteRequest(Long friendMemberId, Boolean isFavorite) {
		this.friendMemberId = friendMemberId;
		this.isFavorite = isFavorite;
	}

}
