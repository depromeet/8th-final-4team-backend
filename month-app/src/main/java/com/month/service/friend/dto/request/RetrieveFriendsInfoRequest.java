package com.month.service.friend.dto.request;

import com.month.service.friend.FriendsSortBy;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class RetrieveFriendsInfoRequest {

	@NotNull
	private FriendsSortBy sortBy;

}
