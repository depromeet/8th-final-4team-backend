package com.month.service.friend.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class DeleteFriendMapperRequest {

	@NotNull
	private Long friendMemberId;

}
