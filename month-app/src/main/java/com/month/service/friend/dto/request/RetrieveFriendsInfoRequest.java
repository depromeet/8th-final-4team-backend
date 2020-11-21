package com.month.service.friend.dto.request;

import com.month.service.friend.FriendListSortType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class RetrieveFriendsInfoRequest {

	@NotNull
	private FriendListSortType sortBy;

}
