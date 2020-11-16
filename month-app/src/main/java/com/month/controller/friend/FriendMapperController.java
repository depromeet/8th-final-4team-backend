package com.month.controller.friend;

import com.month.config.resolver.LoginMember;
import com.month.controller.ApiResponse;
import com.month.service.friend.FriendMapperService;
import com.month.service.friend.dto.request.*;
import com.month.service.friend.dto.response.FriendInfoResponse;
import com.month.service.friend.dto.response.FriendSimpleInfoResponse;
import com.month.type.session.MemberSession;
import io.swagger.annotations.ApiImplicitParam;
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
	@ApiImplicitParam(name = "Authorization", value = "Bearer Token", required = true, paramType = "header")
	@PostMapping("/api/v1/member/friend")
	public ApiResponse<String> registerFriend(@Valid @RequestBody RegisterFriendRequest request, @LoginMember MemberSession memberSession) {
		friendMapperService.registerFriend(request, memberSession.getMemberId());
		return ApiResponse.OK;
	}

	@ApiOperation("나의 친구 리스트를 조회하는 API")
	@ApiImplicitParam(name = "Authorization", value = "Bearer Token", required = true, paramType = "header")
	@GetMapping("/api/v1/member/friend/list")
	public ApiResponse<List<FriendSimpleInfoResponse>> retrieveMyFriendsList(@Valid RetrieveFriendsInfoRequest request, @LoginMember MemberSession memberSession) {
		return ApiResponse.of(friendMapperService.retrieveMyFriendsList(request.getSortBy(), memberSession.getMemberId()));
	}

	@ApiOperation("친구를 즐겨찾기 ON/OFF 하는 API")
	@ApiImplicitParam(name = "Authorization", value = "Bearer Token", required = true, paramType = "header")
	@PutMapping("/api/v1/member/friend/favorite")
	public ApiResponse<String> updateFavorite(@Valid @RequestBody UpdateFavoriteRequest request, @LoginMember MemberSession memberSession) {
		friendMapperService.updateFavorite(request, memberSession.getMemberId());
		return ApiResponse.OK;
	}

	@ApiOperation("친구 목록에서 삭제하는 API")
	@ApiImplicitParam(name = "Authorization", value = "Bearer Token", required = true, paramType = "header")
	@DeleteMapping("/api/v1/member/friend")
	public ApiResponse<String> unRegisterFriend(@Valid UnRegisterFriendRequest request, @LoginMember MemberSession memberSession) {
		friendMapperService.unRegisterFriend(request.getFriendMemberId(), memberSession.getMemberId());
		return ApiResponse.OK;
	}

	@ApiOperation("친구의 상세정보를 조회하는 API")
	@ApiImplicitParam(name = "Authorization", value = "Bearer Token", required = true, paramType = "header")
	@GetMapping("/api/v1/member/friend")
	public ApiResponse<FriendInfoResponse> retrieveFriendInfo(@Valid RetrieveFriendInfoRequest request, @LoginMember MemberSession memberSession) {
		return ApiResponse.of(friendMapperService.retrieveFriendInfo(request, memberSession.getMemberId()));
	}

}
