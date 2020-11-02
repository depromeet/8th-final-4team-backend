package com.month.controller.friend;

import com.month.config.resolver.LoginMember;
import com.month.controller.ApiResponse;
import com.month.service.friend.FriendListSortType;
import com.month.service.friend.FriendMapperService;
import com.month.service.friend.dto.request.CreateFriendMapperRequest;
import com.month.service.friend.dto.response.FriendMemberInfoResponse;
import com.month.type.session.MemberSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class FriendMapperController {

	private final FriendMapperService friendMapperService;

	@PostMapping("/api/v1/member/friend")
	public ApiResponse<String> createFriendMapper(@Valid @RequestBody CreateFriendMapperRequest request, @LoginMember MemberSession memberSession) {
		friendMapperService.createFriend(request, memberSession.getMemberId());
		return ApiResponse.OK;
	}

	@GetMapping("/api/v1/member/friend/list")
	public ApiResponse<List<FriendMemberInfoResponse>> retrieveFriendsInfoResponse(@Valid @RequestParam FriendListSortType sortBy, @LoginMember MemberSession memberSession) {
		return ApiResponse.of(friendMapperService.retrieveMyFriendsInfo(sortBy, memberSession.getMemberId()));
	}

	// TODO 친구를 즐겨찾기 ON/OFF 하는 기능

	// TODO 친구의 정보를 불러오는 API + (함께한 정보들도 포함)

	// TODO 친구목록에서 삭제하는 API

}
