package com.month.controller.friend;

import com.month.config.resolver.LoginMember;
import com.month.controller.ApiResponse;
import com.month.service.friend.FriendListSortType;
import com.month.service.friend.FriendMapperService;
import com.month.service.friend.dto.request.CreateFriendMapperRequest;
import com.month.service.friend.dto.request.UpdateFriendFavoriteRequest;
import com.month.service.friend.dto.response.FriendMemberInfoResponse;
import com.month.type.session.MemberSession;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class FriendMapperController {

	private final FriendMapperService friendMapperService;

	@ApiOperation("새로운 친구를 등록하는 API")
	@PostMapping("/api/v1/member/friend")
	public ApiResponse<String> createFriendMapper(@Valid @RequestBody CreateFriendMapperRequest request, @LoginMember MemberSession memberSession) {
		friendMapperService.createFriend(request, memberSession.getMemberId());
		return ApiResponse.OK;
	}

	@ApiOperation("나의 친구 리스트를 조회하는 API")
	@GetMapping("/api/v1/member/friend/list")
	public ApiResponse<List<FriendMemberInfoResponse>> retrieveFriendsInfoResponse(@Valid @RequestParam FriendListSortType sortBy, @LoginMember MemberSession memberSession) {
		return ApiResponse.of(friendMapperService.retrieveMyFriendsInfo(sortBy, memberSession.getMemberId()));
	}

	@ApiOperation("친구를 즐겨찾기 ON/OFF 하는 API")
	@PutMapping("/api/v1/member/friend/favorite")
	public ApiResponse<String> updateFriendFavorite(@Valid @RequestBody UpdateFriendFavoriteRequest request, @LoginMember MemberSession memberSession) {
		friendMapperService.updateFriendFavorite(request, memberSession.getMemberId());
		return ApiResponse.OK;
	}

	@ApiOperation("친구 목록에서 삭제하는 API")
	@DeleteMapping("/api/v1/member/friend")
	public ApiResponse<String> deleteFriendMapper(@Valid @RequestParam Long friendMemberId, @LoginMember MemberSession memberSession) {
		friendMapperService.deleteFriendMapper(friendMemberId, memberSession.getMemberId());
		return ApiResponse.OK;
	}

	// TODO 친구의 정보를 불러오는 API + (함께한 목표 정보들도 포함) - 아직 미정이라 차후 구현

}
